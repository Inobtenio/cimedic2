package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.activities.SearchActivity;
import com.development.unobtainium.cimedic2.fragments.RegistrationFragment;
import com.development.unobtainium.cimedic2.fragments.SearchFragment;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Patient;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by unobtainium on 24/09/16.
 */
public class PatientsTableAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Patient> patientList = new ArrayList<Patient>();

    public PatientsTableAdapter(Context mContext, ArrayList<Patient> relatives) {
        patientList = relatives;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView picture;
        ImageView current;
        TextView name;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Log.e("PATIENTS", new Gson().toJson(patientList));
        Holder holder = new Holder();
        View cell;
        cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.picture = (ImageView) cell.findViewById(R.id.patient_photo);
        holder.current = (ImageView) cell.findViewById(R.id.badge);
        if (!patientList.get(position).getImage().equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(60)
                    .oval(true)
                    .build();
            Picasso.with(parent.getContext())
                    .load(patientList.get(position).getImage())
                    .fit().centerCrop()
                    .transform(transformation)
                    .into(holder.picture);
        } else {
            holder.picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.patient_placeholder));
        };
        if (String.valueOf(patientList.get(position).getId()).equals(loggedPatientId)){
            Transformation transformationBadge = new RoundedTransformationBuilder()
                    .cornerRadiusDp(8)
                    .oval(true)
                    .build();
            Picasso.with(parent.getContext())
                    .load(String.valueOf(parent.getResources().getDrawable(R.drawable.patient_placeholder)))
                    .fit().centerCrop()
                    .transform(transformationBadge)
                    .into(holder.current);
        } else {
            holder.current.setVisibility(View.INVISIBLE);
        }
        Log.e("LOGGED PATIENT", loggedPatientId);
        Log.e("PATIENT", String.valueOf(patientList.get(position).getId()));
        holder.name = (TextView) cell.findViewById(R.id.patient_name);
        holder.name.setText(patientList.get(position).getNames());
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientSessionManager psm = new PatientSessionManager(parent.getContext());
                psm.createPatientLoginSession(patientList.get(position).getId(), patientList.get(position).getEmail(), patientList.get(position).getNames(), patientList.get(position).getImage());
                ((SearchActivity) mContext).updateProfilePicture();
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SearchFragment llf = new SearchFragment();
                ft.addToBackStack(null);
                ft.replace(R.id.currentFragment, llf);
                ft.commit();
            }
        });
        cell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mContext, v);
                menu.getMenuInflater().inflate(R.menu.patient_actions, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.patient_edit_action) {
                            FragmentManager fm = ((Activity) mContext).getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            RegistrationFragment llf = RegistrationFragment.newInstance(patientList.get(position), patientList.get(position).getPrincipal(), String.valueOf(patientList.get(position).getId()) , true);
                            ft.addToBackStack("main");
                            ft.replace(R.id.currentFragment, llf);
                            ft.commit();
                        } else if (id == R.id.patient_delete_action) {
                            Toast.makeText(mContext, "Aun no implementado", Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
                menu.show();
                return true;
            }
        });
        return cell;
    }
}
