package com.development.unobtainium.cimedic2.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Appointment;
import com.development.unobtainium.cimedic2.models.Doctor;
import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.models.Schedule;
import com.development.unobtainium.cimedic2.responses.OkResponse;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;
import com.development.unobtainium.cimedic2.retrofit.ServicesInterface;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentPreviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentPreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentPreviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String DOCTOR_PICTURE = "param1";
//    private static final String DOCTOR_NAME = "param2";

    private Doctor mDoctor;
    private String mPatientName;
    private String mPatientPicture;
    private Schedule mSchedule;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    private ImageView doctorPicture;
    private ImageView patientPicture;
    private TextView doctorName;
    private TextView doctorSpecialty;
    private TextView doctorSchedule;
    private TextView patientName;
    private Button registerButton;
    private SchedulesFragment sFragment;
    private Appointment mAppointment = null;
    private String message;
    private RegisterAppointmentTask mAppointmentTask;
    final String api_endpoint = "https://medic-1.herokuapp.com/api/v1/";

    private OnFragmentInteractionListener mListener;

    public AppointmentPreviewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppointmentPreviewFragment newInstance(SchedulesFragment schFragment, String patientName, String patientPicture, Schedule schedule) {
        AppointmentPreviewFragment fragment = new AppointmentPreviewFragment();
        fragment.sFragment = schFragment;
        fragment.mDoctor = schFragment.mDoctor;
        fragment.mPatientName = patientName;
        fragment.mPatientPicture = patientPicture;
        fragment.mSchedule = schedule;
        return fragment;
    }

    public static AppointmentPreviewFragment newInstance(Appointment appointment, String patientName, String patientPicture, Schedule schedule) {
        AppointmentPreviewFragment fragment = new AppointmentPreviewFragment();
        fragment.mAppointment = appointment;
        fragment.mDoctor = appointment.getDoctor();
        fragment.mPatientName = patientName;
        fragment.mPatientPicture = patientPicture;
        fragment.mSchedule = schedule;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mProgressView = getView().findViewById(R.id.appointment_progress);
        doctorPicture = (ImageView) getView().findViewById(R.id.app_doctor_picture);
        patientPicture = (ImageView) getView().findViewById(R.id.app_patient_picture);
        doctorName = (TextView) getView().findViewById(R.id.app_doctor_name);
        doctorSchedule = (TextView) getView().findViewById(R.id.app_doctor_schedule);
        patientName = (TextView) getView().findViewById(R.id.app_patient_name);
        registerButton = (Button) getView().findViewById(R.id.app_book_button);
        doctorName.setText(mDoctor.getName());
        doctorSchedule.setText(mSchedule.fullHoursString());
        if (!mDoctor.getImage().equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(60)
                    .oval(true)
                    .build();
            Picasso.with(getContext())
                    .load(mDoctor.getImage())
                    .fit().centerCrop()
                    .transform(transformation)
                    .into(doctorPicture);
        }
        showProgress(false);
        updatePatientData();
        final AppointmentPreviewFragment that = this;
        patientPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientsFragment fragment = PatientsFragment.newInstance(false, that);
                fragment.show(getFragmentManager(), "Dialog");
            }
        });
        if (mAppointment != null){
            registerButton.setText("EDITAR CITA");
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAppointment != null){
                    if (mAppointment.getStatus() == 1){
                        Toast.makeText(getContext(), "No puede editar una cita pasada", Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        SchedulesFragment llf = SchedulesFragment.newInstance(mAppointment.getClinic().getId(), mAppointment.getSpecialty().getId(), mAppointment.getDoctor());
                        ft.replace(R.id.currentFragment, llf);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else {
                    showProgress(true);
                    mAppointmentTask = new RegisterAppointmentTask();
                    mAppointmentTask.execute((Void) null);
                }
            }
        });

//        mSpecialtiesTask = new SpecialtiesTask();
//        mSpecialtiesTask.execute((Void) null);
        super.onActivityCreated(savedInstanceState);
    }

    public void updatePatientData(){
        mPatientName = PatientSessionManager.getInstance(getContext()).getLoggedPatientName();
        mPatientPicture = PatientSessionManager.getInstance(getContext()).getLoggedPatientImage();
        if (!mPatientName.equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(60)
                    .oval(true)
                    .build();
            Picasso.with(getContext())
                    .load(mPatientPicture)
                    .fit().centerCrop()
                    .transform(transformation)
                    .into(patientPicture);
        }
        patientName.setText(mPatientName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_preview, container, false);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public class RegisterAppointmentTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getContext();

        RegisterAppointmentTask() {
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

            String clinic_id = sFragment.clinic_id;
            String specialty_id = sFragment.specialty_id;
            if (mAppointment != null){
                clinic_id = String.valueOf(mAppointment.getClinic().getId());
                specialty_id = String.valueOf(mAppointment.getSpecialty().getId());
            }
            final Call<OkResponse> call = apiService.createAppointment(PatientSessionManager.getInstance(getContext()).getLoggedPatientId(), clinic_id, specialty_id, String.valueOf(mDoctor.getId()), String.valueOf(mSchedule.getId()));
            try {
                Response<OkResponse> response = call.execute();
                message = response.body().message;
                sError = response.body().error;
            } catch (IOException e) {
                error = e.getMessage();
            }
            return error.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAppointmentTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    HistoryFragment llf = new HistoryFragment();
                    ft.replace(R.id.currentFragment, llf);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    Toast.makeText(getContext(), sError.getDescription(), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAppointmentTask = null;
            showProgress(false);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
