package com.development.unobtainium.cimedic2.fragments;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RegistrationFragment extends Fragment {
    private static final String SHOW_PASSWORD = "show_password";
    private static final String PATIENT_ID = "patient_id";
    private OnFragmentInteractionListener mListener;
    private PatientRegisterTask mRegisterTask = null;
    final String api_endpoint = "http://192.168.1.105:3000/api/v1/patients/";
    private Patient loggedPatient;
    private Patient patient;
    private Boolean authenticated = false;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    private EditText bithdayInput;
    Gson gson;
    Calendar myCalendar = Calendar.getInstance();

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance(Boolean show, String id) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_PASSWORD, show);
        args.putString(PATIENT_ID, id);
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
        CheckBox termsCheckbox = (CheckBox) getView().findViewById(R.id.terms_checkbox);
        Button registerButton = (Button) getView().findViewById(R.id.register_button);

        if (getArguments() != null) {
            if (!getArguments().getBoolean(SHOW_PASSWORD)){
                ((ViewGroup) passwordInput.getParent()).removeView(passwordInput);
                passwordLabel.setVisibility(View.INVISIBLE);
            }
        }

//        Log.e("dsadsadasda", gson.toJson(prevIntent.getExtras()));

        ArrayAdapter<CharSequence> docTypes = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.doc_types_array, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> departments = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.departments_array, R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> districts = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.districts_array, R.layout.support_simple_spinner_dropdown_item);

        docTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        docTypeSpinner.setAdapter(docTypes);
        departmentSpinner.setAdapter(departments);
        districtSpinner.setAdapter(districts);

        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient = new Patient(namesInput.getText().toString(), lastNameInput.getText().toString(), mothersLastNameInput.getText().toString(), bithdayInput.getText().toString(), districtSpinner.getSelectedItemPosition(), documentNumberInput.getText().toString(), docTypeSpinner.getSelectedItemPosition(), addressInput.getText().toString(), emailInput.getText().toString(), passwordInput.getText().toString());
                showProgress(true);
                mRegisterTask = new PatientRegisterTask(patient);
                mRegisterTask.execute((Void) null);
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

        String myFormat = "EEE, dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        bithdayInput.setText(sdf.format(myCalendar.getTime()));
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public class PatientRegisterTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getActivity().getApplicationContext();
        final Patient mPatient;

        PatientRegisterTask(Patient patient) {
            mPatient = patient;
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


            final Call<PatientResponse> call = apiService.registerPatient(getArguments().getString(PATIENT_ID),patient);
            try {
                Response<PatientResponse> response = call.execute();
                loggedPatient = response.body().patient;
                sError = response.body().error;
                authenticated = true;
            } catch (IOException e) {
                error = e.getMessage();
                authenticated = false;
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
                        psm.createPatientLoginSession(loggedPatient.getId(), loggedPatient.getEmail(), loggedPatient.getNames());
                        startActivity(new Intent(getContext(), SearchActivity.class));
                    } else {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

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
