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
import com.development.unobtainium.cimedic2.adapters.PrescriptionsListAdapter;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Prescription;
import com.development.unobtainium.cimedic2.responses.PrescriptionsResponse;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;
import com.development.unobtainium.cimedic2.retrofit.ServicesInterface;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrescriptionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private PrescriptionsTask mPrescriptionsTask = null;
    final String api_endpoint = "https://medic-1.herokuapp.com/api/v1/"; //"http://192.168.1.105:3000/api/v1/";
    private ArrayList<Prescription> prescriptions;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    Gson gson;
    ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PrescriptionsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PrescriptionsFragment newInstance() {
        PrescriptionsFragment fragment = new PrescriptionsFragment();
        return fragment;
    }

    public class PrescriptionsTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getContext();

        PrescriptionsTask() {
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

            final Call<PrescriptionsResponse> call = apiService.getPrescriptions(PatientSessionManager.getInstance(getContext()).getLoggedPatientId());
            try {
                Response<PrescriptionsResponse> response = call.execute();
                prescriptions = response.body().prescriptions;
                sError = response.body().error;
            } catch (IOException e) {
                error = e.getMessage();
            }
            return error.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mPrescriptionsTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
//                    Toast.makeText(getContext(), gson.toJson(schedules), Toast.LENGTH_LONG).show();
                    listView = (ListView) getView().findViewById(R.id.prescription_list);
                    if (listView != null){
                        listView.setAdapter(new PrescriptionsListAdapter(getContext(), prescriptions));
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
            mPrescriptionsTask = null;
            showProgress(false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        listView = (ListView) getView().findViewById(R.id.prescription_list);
        mProgressView = getView().findViewById(R.id.prescription_progress);
        showProgress(true);
        mPrescriptionsTask = new PrescriptionsTask();
        mPrescriptionsTask.execute((Void) null);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prescriptions, container, false);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
