package com.development.unobtainium.cimedic2.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.activities.RegistrationActivity;
import com.development.unobtainium.cimedic2.activities.SearchActivity;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.responses.PatientResponse;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;
import com.development.unobtainium.cimedic2.retrofit.ServicesInterface;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationFragment extends Fragment {
    private static final String SHOW_PASSWORD = "show_password";
    private static final String PATIENT_ID = "patient_id";
    private static final String EDIT_PATIENT_ID = "edit_patient_id";
    private OnFragmentInteractionListener mListener;
    private PatientRegisterTask mRegisterTask = null;
    final String api_endpoint = "https://medic-1.herokuapp.com/api/v1/patients/"; //"http://192.168.1.105:3000/api/v1/patients/";
    private Patient loggedPatient;
    private Patient editingPatient;
    private Patient patient;
    private Boolean authenticated = false;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    private EditText bithdayInput;
    private ImageView patientPicture;
    final Gson gson = new Gson();
    private File picture = null;
    private String relationship_id;
    Calendar myCalendar = Calendar.getInstance();
    MultipartBody.Part body;
    RequestBody requestFile;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance(Patient patient, Boolean show, String id, Boolean edit) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        fragment.editingPatient = patient;
        args.putBoolean(SHOW_PASSWORD, show);
        if (edit){
            args.putString(EDIT_PATIENT_ID, id);
        } else {
            args.putString(PATIENT_ID, id);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mProgressView = getView().findViewById(R.id.registration_progress);
        patientPicture = (ImageView) getView().findViewById(R.id.profile_picture);
        showProgress(false);
        final EditText namesInput = (EditText) getView().findViewById(R.id.names_input);
        final EditText lastNameInput = (EditText) getView().findViewById(R.id.last_name_input);
        final EditText mothersLastNameInput = (EditText) getView().findViewById(R.id.mothers_last_name_input);
        bithdayInput = (EditText) getView().findViewById(R.id.birthday_input);
        final EditText documentNumberInput = (EditText) getView().findViewById(R.id.doc_number_input);
        final EditText addressInput = (EditText) getView().findViewById(R.id.address_input);
        final EditText emailInput = (EditText) getView().findViewById(R.id.email_input);
        final EditText passwordInput = (EditText) getView().findViewById(R.id.password_input);
        final TextView passwordLabel = (TextView) getView().findViewById(R.id.password_label);
        final Spinner docTypeSpinner = (Spinner) getView().findViewById(R.id.doc_type_spinner);
        Spinner departmentSpinner = (Spinner) getView().findViewById(R.id.department_spinner);
        final Spinner districtSpinner = (Spinner) getView().findViewById(R.id.district_spinner);
        final CheckBox termsCheckbox = (CheckBox) getView().findViewById(R.id.terms_checkbox);
        final TextView relationshipLabel = (TextView) getView().findViewById(R.id.relationship_label);
        final Spinner relationshipSpinner = (Spinner) getView().findViewById(R.id.relationship_spinner);
        Button registerButton = (Button) getView().findViewById(R.id.register_button);

        if (getArguments() != null) {
            if (!getArguments().getBoolean(SHOW_PASSWORD)){
                ((ViewGroup) passwordInput.getParent()).removeView(passwordInput);
                passwordLabel.setVisibility(View.INVISIBLE);
            }
        }

        ArrayAdapter<CharSequence> docTypes = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.doc_types_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> departments = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.departments_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> districts = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.districts_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> relationships = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.relationsips_array, R.layout.spinner_item);

        docTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relationships.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        docTypeSpinner.setAdapter(docTypes);
        departmentSpinner.setAdapter(departments);
        districtSpinner.setAdapter(districts);
        relationshipSpinner.setAdapter(relationships);

        if (editingPatient != null){
            if (!editingPatient.getImage().equals("")) {
                Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(60)
                        .oval(true)
                        .build();
                Picasso.with(getContext())
                        .load(editingPatient.getImage())
                        .fit().centerCrop()
                        .transform(transformation)
                        .into(patientPicture);
            } else {
                patientPicture.setImageDrawable(getResources().getDrawable(R.drawable.patient_placeholder));
            };
            namesInput.setText(editingPatient.getNames());
            lastNameInput.setText(editingPatient.getLast_name());
            mothersLastNameInput.setText(editingPatient.getMothers_last_name());
            bithdayInput.setText(editingPatient.getBirthday());
            docTypeSpinner.setSelection(editingPatient.getDocument_type() - 1);
            documentNumberInput.setText(editingPatient.getDocument_number());
            districtSpinner.setSelection(editingPatient.getDistrict_id() - 1);
            addressInput.setText(editingPatient.getAddress());
            emailInput.setText(editingPatient.getEmail());
            passwordInput.setText(editingPatient.getPassword());
            termsCheckbox.setVisibility(View.INVISIBLE);
            if (editingPatient.getPrincipal()){
                relationshipSpinner.setVisibility(View.INVISIBLE);
                relationshipLabel.setVisibility(View.INVISIBLE);
            }
        }

        if (!PatientSessionManager.getInstance(getContext()).isPatientLoggedIn()){
            relationshipSpinner.setVisibility(View.INVISIBLE);
            relationshipLabel.setVisibility(View.INVISIBLE);
        }

        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termsCheckbox.isChecked() || editingPatient != null){
                    relationship_id = String.valueOf(relationshipSpinner.getSelectedItemPosition()+1);
                    patient = new Patient(namesInput.getText().toString(), lastNameInput.getText().toString(), mothersLastNameInput.getText().toString(), bithdayInput.getText().toString(), districtSpinner.getSelectedItemPosition()+1, documentNumberInput.getText().toString(), docTypeSpinner.getSelectedItemPosition()+1, addressInput.getText().toString(), emailInput.getText().toString(), passwordInput.getText().toString());
                    showProgress(true);
                    mRegisterTask = new PatientRegisterTask(patient, relationship_id);
                    mRegisterTask.execute((Void) null);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Debe aceptar los términos de uso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bithdayInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        patientPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && null != data) {

            String uri = getRealPathFromURI_API19(getActivity().getApplicationContext(), data.getData());
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(70)
                    .oval(true)
                    .build();
            Picasso.with(getActivity().getApplicationContext())
                    .load(data.getData())
                    .fit().centerCrop()
                    .transform(transformation)
                    .into(patientPicture);
            picture = new File(uri);
        }
    }

    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("es", "ES"));

        bithdayInput.setText(sdf.format(myCalendar.getTime()));
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public class PatientRegisterTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getActivity().getApplicationContext();
        final Patient mPatient;
        final String relationship;

        PatientRegisterTask(Patient patient, String relationship_id) {
            mPatient = patient;
            relationship = relationship_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            final String BASE_URL = api_endpoint;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ServicesInterface apiService =
                    retrofit.create(ServicesInterface.class);

            if (picture != null){
                requestFile = RequestBody.create(MediaType.parse("image/jpg"), picture);
                body = MultipartBody.Part.createFormData("patient[picture]", picture.getName(), requestFile);
            }
            RequestBody names = RequestBody.create(MediaType.parse("text/plain"), patient.getNames());
            RequestBody last_name = RequestBody.create(MediaType.parse("text/plain"), patient.getLast_name());
            RequestBody mothers_last_name = RequestBody.create(MediaType.parse("text/plain"), patient.getMothers_last_name());
            RequestBody birthday = RequestBody.create(MediaType.parse("text/plain"), patient.getBirthday());
            RequestBody district_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(patient.getDistrict_id()));
            RequestBody document_number = RequestBody.create(MediaType.parse("text/plain"), patient.getDocument_number());
            RequestBody document_type = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(patient.getDocument_type()));
            RequestBody address = RequestBody.create(MediaType.parse("text/plain"), patient.getAddress());
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), patient.getEmail());
            RequestBody password = RequestBody.create(MediaType.parse("text/plain"), patient.getPassword());
            RequestBody relationship_id = RequestBody.create(MediaType.parse("text/plain"), relationship);

            if (getArguments().getString(EDIT_PATIENT_ID) == null) {
                final Call<PatientResponse> call = apiService.registerPatient(getArguments().getString(PATIENT_ID), body, names, last_name, mothers_last_name, birthday, district_id, document_number, document_type, address, email, password, relationship_id);
                try {
                    Response<PatientResponse> response = call.execute();
                    loggedPatient = response.body().patient;
                    sError = response.body().error;
                    authenticated = true;
                } catch (IOException e) {
                    error = e.getMessage();
                    authenticated = false;
                }
            } else {
                final Call<PatientResponse> call = apiService.editPatient(getArguments().getString(EDIT_PATIENT_ID), body, names, last_name, mothers_last_name, birthday, district_id, document_number, document_type, address, email, password);
                try {
                    Response<PatientResponse> response = call.execute();
                    loggedPatient = response.body().patient;
                    sError = response.body().error;
                    authenticated = true;
                } catch (IOException e) {
                    error = e.getMessage();
                    authenticated = false;
                }
            }
            return authenticated;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
                    if (getActivity() instanceof RegistrationActivity){
                        PatientSessionManager psm = new PatientSessionManager(context);
                        psm.createPatientLoginSession(loggedPatient.getId(), loggedPatient.getEmail(), loggedPatient.getNames(), loggedPatient.getImage());
                        Toast.makeText(getActivity().getApplicationContext(), "Correctamente registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), SearchActivity.class));
                    } else {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Toast.makeText(getActivity().getApplicationContext(), "Correctamente guardado", Toast.LENGTH_SHORT).show();
                        PatientsFragment llf = new PatientsFragment();
                        ft.replace(R.id.currentFragment, llf);
                        ft.commit();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), sError.getDescription(), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
