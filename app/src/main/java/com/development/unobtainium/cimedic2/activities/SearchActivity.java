package com.development.unobtainium.cimedic2.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.fragments.AppointmentPreviewFragment;
import com.development.unobtainium.cimedic2.fragments.DoctorsFragment;
import com.development.unobtainium.cimedic2.fragments.HistoryFragment;
import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.PatientsFragment;
import com.development.unobtainium.cimedic2.fragments.RegistrationFragment;
import com.development.unobtainium.cimedic2.fragments.SchedulesFragment;
import com.development.unobtainium.cimedic2.fragments.SearchFragment;
import com.development.unobtainium.cimedic2.fragments.SpecialtiesFragment;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.OnFragmentInteractionListener, PatientsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener, RegistrationFragment.OnFragmentInteractionListener, SpecialtiesFragment.OnFragmentInteractionListener, DoctorsFragment.OnFragmentInteractionListener, SchedulesFragment.OnFragmentInteractionListener, AppointmentPreviewFragment.OnFragmentInteractionListener {

    private ImageView currentPatientPicture;
    private String imageUrl;
    private TextView currentPatientEmail;
    private TextView currentPatientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SearchFragment llf = new SearchFragment();
        ft.replace(R.id.currentFragment, llf);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        currentPatientEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.patientEmail);
        currentPatientName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.patientName);
        currentPatientPicture = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.patientPicture);
        updateProfilePicture();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void updateProfilePicture(){
        PatientSessionManager psm = PatientSessionManager.getInstance(getApplicationContext());
        imageUrl = psm.getLoggedPatientImage();
        if (!imageUrl.equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(45)
                    .oval(true)
                    .build();
            Picasso.with(getApplicationContext())
                    .load(PatientSessionManager.getInstance(getApplicationContext()).getLoggedPatientImage())
                    .fit().centerCrop()
                    .transform(transformation)
                    .into(currentPatientPicture);
        } else {
            currentPatientPicture.setImageDrawable(getResources().getDrawable(R.drawable.patient_placeholder));
        };
        currentPatientName.setText(psm.getLoggedPatientName());
        currentPatientEmail.setText(psm.getLoggedPatientEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            PatientSessionManager.getInstance(getApplicationContext()).ultimateLogout();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_search) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SearchFragment llf = new SearchFragment();
            ft.addToBackStack(null);
            ft.replace(R.id.currentFragment, llf);
            ft.commit();
        } else if (id == R.id.nav_patients) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PatientsFragment llf = new PatientsFragment();
            ft.addToBackStack(null);
            ft.replace(R.id.currentFragment, llf);
            ft.commit();
        } else if (id == R.id.nav_history) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            HistoryFragment llf = new HistoryFragment();
            ft.addToBackStack(null);
            ft.replace(R.id.currentFragment, llf);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
