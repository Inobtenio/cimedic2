package com.development.unobtainium.cimedic2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.adapters.SchedulesListAdapter;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Doctor;
import com.development.unobtainium.cimedic2.models.Schedule;
import com.development.unobtainium.cimedic2.responses.SchedulesResponse;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;
import com.development.unobtainium.cimedic2.retrofit.ServicesInterface;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SchedulesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SchedulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLINIC_ID = "clinic_id";
    private static final String SPECIALTY_ID = "specialty_id";
    private static final String DOCTOR_ID = "doctor_id";
    private SchedulesTask mSchedulesTask = null;
    final String api_endpoint = "https://medic-1.herokuapp.com/api/v1/"; //"http://192.168.1.105:3000/api/v1/";
    private ArrayList<Schedule> schedules;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    public Doctor mDoctor;
    private ImageView doctorPicture;
    public static SchedulesFragment sFragment;
    Gson gson;
    ListView listView;

    // TODO: Rename and change types of parameters
    public String clinic_id;
    public String specialty_id;
    public String doctor_id;

    private OnFragmentInteractionListener mListener;

    public SchedulesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SchedulesFragment newInstance(Integer clinic_id, Integer specialty_id, Doctor doctor) {
        SchedulesFragment fragment = new SchedulesFragment();
        Bundle args = new Bundle();
        args.putInt(CLINIC_ID, clinic_id);
        args.putInt(SPECIALTY_ID, specialty_id);
        fragment.mDoctor = doctor;
        fragment.setArguments(args);
        sFragment = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clinic_id = String.valueOf(getArguments().getInt(CLINIC_ID));
            specialty_id = String.valueOf(getArguments().getInt(SPECIALTY_ID));
            doctor_id = String.valueOf(getArguments().getInt(DOCTOR_ID));
        }
    }

    public class SchedulesTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getContext();

        SchedulesTask() {
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

            final Call<SchedulesResponse> call = apiService.getSchedules(PatientSessionManager.getInstance(getContext()).getLoggedPatientId(), clinic_id, specialty_id, String.valueOf(mDoctor.getId()));
            try {
                Response<SchedulesResponse> response = call.execute();
                schedules = response.body().schedules;
                sError = response.body().error;
            } catch (IOException e) {
                error = e.getMessage();
            }
            return error.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSchedulesTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
//                    Toast.makeText(getContext(), gson.toJson(schedules), Toast.LENGTH_LONG).show();
                    Multimap<String, Schedule> map = HashMultimap.create();
                    for (Schedule schedule : schedules){
                        String day = schedule.hashKey();

                        map.put(day, schedule);
                    }
                    Set keySet = map.keySet();
                    Iterator keyIterator = keySet.iterator();
                    while (keyIterator.hasNext() ) {
                        String key = (String) keyIterator.next();
                        Collection<Schedule> values = map.get(key);
//                        Collections.sort((List) values);
                        listView = (ListView) getView().findViewById(getResources().getIdentifier(key, "id", getActivity().getPackageName()));
                        if (listView != null){
                            listView.setAdapter(new SchedulesListAdapter(getContext(), new ArrayList<Schedule>(values), sFragment));
                        }
                    }
                } else {
                    Toast.makeText(getContext(), sError.getDescription(), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mSchedulesTask = null;
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mProgressView = getView().findViewById(R.id.schedules_progress);
        doctorPicture = (ImageView) getView().findViewById(R.id.sch_doctor_picture);
        if (!mDoctor.getImage().equals("")) {
            Transformation transformationBadge = new RoundedTransformationBuilder()
                    .cornerRadiusDp(50)
                    .oval(true)
                    .build();
            Picasso.with(getContext())
                    .load(mDoctor.getImage())
                    .fit().centerCrop()
                    .transform(transformationBadge)
                    .into(doctorPicture);
        } else {
            doctorPicture.setImageDrawable(getResources().getDrawable(R.drawable.patient_placeholder));
        };
//        listView = (ListView) getView().findViewById(R.id.doctors_list);
        showProgress(true);
        mSchedulesTask = new SchedulesTask();
        mSchedulesTask.execute((Void) null);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedules, container, false);
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
