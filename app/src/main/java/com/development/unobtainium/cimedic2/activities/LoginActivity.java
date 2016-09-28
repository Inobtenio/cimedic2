package com.development.unobtainium.cimedic2.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.HistoryFragment;
import com.development.unobtainium.cimedic2.fragments.SearchFragment;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.responses.PatientResponse;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;
import com.development.unobtainium.cimedic2.retrofit.ServicesInterface;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    final Gson gson = new Gson();
    private Patient loggedPatient;
    private Boolean authenticated = false;
    private ServiceError sError;
    private String error = "";
    private String obtainedId = "";
    final String api_endpoint = "http://192.168.10.124:3000/api/v1/patients/";


    private PatientLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login_form || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        Button mEmailSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
        mProgressView = findViewById(R.id.login_progress);

        PatientSessionManager usm = PatientSessionManager.getInstance(getApplicationContext());
//        if (usm.isUserLoggedIn()){
//            Intent intent = new Intent(getApplicationContext(), ChoosingActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else {
            if (!(password.matches("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$")) ){
                mPasswordView.setError(getString(R.string.error_incorrect_format));
                focusView = mPasswordView;
                cancel = true;
            }
        }

        /* else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new PatientLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class PatientLoginTask extends AsyncTask<Void, Void, Boolean> {
        final Context context = getApplicationContext();
        private final String mEmail;
        private final String mPassword;

        PatientLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
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


            final Call<PatientResponse> call = apiService.getPatient(mEmail, mPassword);
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
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if (sError == null){
                    PatientSessionManager psm = new PatientSessionManager(context);
                    psm.createPatientLoginSession(loggedPatient.getId(), loggedPatient.getEmail(), loggedPatient.getNames());
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), sError.getDescription(), Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}
