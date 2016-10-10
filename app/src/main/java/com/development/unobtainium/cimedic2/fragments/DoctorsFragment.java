package com.development.unobtainium.cimedic2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.adapters.DoctorsListAdapter;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Doctor;
import com.development.unobtainium.cimedic2.responses.DoctorsResponse;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;
import com.development.unobtainium.cimedic2.retrofit.ServicesInterface;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoctorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoctorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLINIC_ID = "clinic_id";
    private static final String SPECIALTY_ID = "specialty_id";
    private DoctorsTask mDoctorsTask = null;
    final String api_endpoint = "https://medic-1.herokuapp.com/api/v1/"; //"http://192.168.1.105:3000/api/v1/";
    private ArrayList<Doctor> doctors;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    Gson gson;
    ListView listView;

    // TODO: Rename and change types of parameters
    private String clinic_id;
    private String specialty_id;

    private OnFragmentInteractionListener mListener;

    public DoctorsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoctorsFragment newInstance(Integer clinic_id, Integer specialty_id) {
        DoctorsFragment fragment = new DoctorsFragment();
        Bundle args = new Bundle();
        args.putInt(CLINIC_ID, clinic_id);
        args.putInt(SPECIALTY_ID, specialty_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clinic_id = String.valueOf(getArguments().getInt(CLINIC_ID));
            specialty_id = String.valueOf(getArguments().getInt(SPECIALTY_ID));
        }
    }

    public class DoctorsTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getContext();

        DoctorsTask() {
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

            final Call<DoctorsResponse> call = apiService.getDoctors(PatientSessionManager.getInstance(getContext()).getLoggedPatientId(), clinic_id, specialty_id);
            try {
                Response<DoctorsResponse> response = call.execute();
                doctors = response.body().doctors;
                sError = response.body().error;
            } catch (IOException e) {
                error = e.getMessage();
            }
            return error.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mDoctorsTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
                    listView.setAdapter(new DoctorsListAdapter(getContext(), doctors) );
                } else {
                    Toast.makeText(getContext(), sError.getDescription(), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mDoctorsTask = null;
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mProgressView = getView().findViewById(R.id.doctors_progress);
        listView = (ListView) getView().findViewById(R.id.doctors_list);
        showProgress(true);
        mDoctorsTask = new DoctorsTask();
        mDoctorsTask.execute((Void) null);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors, container, false);
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
