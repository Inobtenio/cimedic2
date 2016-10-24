package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.DoctorsFragment;
import com.development.unobtainium.cimedic2.fragments.SchedulesFragment;
import com.development.unobtainium.cimedic2.models.Doctor;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by unobtainium on 9/10/16.
 */
public class DoctorsListAdapter extends BaseAdapter {
    private Context mContext;
    private DoctorsFragment dFragment;
    private ArrayList<Doctor> doctorsList = new ArrayList<Doctor>();

    public DoctorsListAdapter(DoctorsFragment fragment, Context mContext, ArrayList<Doctor> doctorsList) {
        this.mContext = mContext;
        this.dFragment = fragment;
        this.doctorsList = doctorsList;
    }


    @Override
    public int getCount() {
        return doctorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {
        ImageView picture;
        TextView name;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
//        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.picture = (ImageView) row.findViewById(R.id.app_doctor_photo);
        if (!doctorsList.get(position).getImage().equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(60)
                    .oval(true)
                    .build();
            Picasso.with(parent.getContext())
                    .load(doctorsList.get(position).getImage())
                    .transform(transformation)
                    .fit().centerCrop()
                    .into(holder.picture);
        } else {
            holder.picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.patient_placeholder));
        };
        holder.name = (TextView) row.findViewById(R.id.doctor_name);
        holder.name.setText(doctorsList.get(position).getName());
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SchedulesFragment llf = SchedulesFragment.newInstance(dFragment.getArguments().getInt("clinic_id"), dFragment.getArguments().getInt("specialty_id"), doctorsList.get(position));
                ft.replace(R.id.currentFragment, llf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return row;
    }
}
