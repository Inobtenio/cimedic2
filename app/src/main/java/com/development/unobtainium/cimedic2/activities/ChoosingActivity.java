package com.development.unobtainium.cimedic2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;

public class ChoosingActivity extends AppCompatActivity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PatientSessionManager psm = PatientSessionManager.getInstance(getApplicationContext());
        if (psm.isPatientLoggedIn()) {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
