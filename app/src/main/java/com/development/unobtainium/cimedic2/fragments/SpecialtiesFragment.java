package com.development.unobtainium.cimedic2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.adapters.SpecialtiesTableAdapter;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Specialty;
import com.development.unobtainium.cimedic2.responses.SpecialtiesResponse;
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
 * {@link SpecialtiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpecialtiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpecialtiesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLINIC_ID = "clinic_id";
    private SpecialtiesTask mSpecialtiesTask = null;
    final String api_endpoint = "https://medic-1.herokuapp.com/api/v1/"; //"http://192.168.1.105:3000/api/v1/";
    private ArrayList<Specialty> specialties;
    private ServiceError sError;
    private String error = "";
    private View mProgressView;
    Gson gson;
    GridView gridView;

    // TODO: Rename and change types of parameters
    private String clinic_id;

    private OnFragmentInteractionListener mListener;

    public SpecialtiesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SpecialtiesFragment newInstance(Integer clinic_id) {
        SpecialtiesFragment fragment = new SpecialtiesFragment();
        Bundle args = new Bundle();
        args.putInt(CLINIC_ID, clinic_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clinic_id = String.valueOf(getArguments().getInt(CLINIC_ID));
        }
    }

    public class SpecialtiesTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getContext();

        SpecialtiesTask() {
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

            final Call<SpecialtiesResponse> call = apiService.getSpecialties(PatientSessionManager.getInstance(getContext()).getLoggedPatientId(), clinic_id);
            try {
                Response<SpecialtiesResponse> response = call.execute();
                specialties = response.body().specialties;
                sError = response.body().error;
            } catch (IOException e) {
                error = e.getMessage();
            }
            return error.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSpecialtiesTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
                    gridView.setAdapter(new SpecialtiesTableAdapter(getContext(), specialties) );
                } else {
                    Toast.makeText(getContext(), sError.getDescription(), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mSpecialtiesTask = null;
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mProgressView = getView().findViewById(R.id.specialties_progress);
        gridView = (GridView) getView().findViewById(R.id.specialties_grid);
        showProgress(true);
        mSpecialtiesTask = new SpecialtiesTask();
        mSpecialtiesTask.execute((Void) null);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specialties, container, false);
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
