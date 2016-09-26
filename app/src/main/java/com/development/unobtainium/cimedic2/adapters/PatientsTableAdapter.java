package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.activities.SearchActivity;
import com.development.unobtainium.cimedic2.fragments.SearchFragment;
import com.development.unobtainium.cimedic2.models.Patient;

import java.util.ArrayList;

/**
 * Created by unobtainium on 24/09/16.
 */
public class PatientsTableAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Patient> patientList = new ArrayList<Patient>();

    public PatientsTableAdapter(Context mContext, ArrayList<Patient> relatives) {
//        patientList.add(patient1);
//        patientList.add(patient2);
//        patientList.add(patient3);
//        patientList.add(patient4);
//        patientList.add(patient5);
//        patientList.add(patient6);
//        patientList.add(patient7);
//        patientList.add(patient8);
//        patientList.add(patient9);
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
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View cell;
        cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        holder.picture = (ImageView) cell.findViewById(R.id.patient_photo);
        holder.picture.setImageDrawable(mContext.getResources().getDrawable(R.drawable.patient_placeholder));
        holder.name = (TextView) cell.findViewById(R.id.patient_name);
        holder.name.setText(patientList.get(position).getNames());
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SearchFragment llf = new SearchFragment();
                ft.replace(R.id.currentFragment, llf);
                ft.commit();
            }
        });
        return cell;
    }
}
