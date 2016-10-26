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
import com.development.unobtainium.cimedic2.fragments.AppointmentPreviewFragment;
import com.development.unobtainium.cimedic2.fragments.SchedulesFragment;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Appointment;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by unobtainium on 17/10/16.
 */
public class AppointmentsListAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    public AppointmentsListAdapter(Context mContext, ArrayList<Appointment> doctorsList) {
        this.mContext = mContext;
        this.appointments = doctorsList;
    }


    @Override
    public int getCount() {
        return appointments.size();
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
        TextView specialty_name;
        TextView schedule_date;
        TextView schedule_time;
        TextView doctor_name;
        ImageView doctor_picture;
        ImageView clinic_picture;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
//        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.doctor_picture = (ImageView) row.findViewById(R.id.app_doctor_photo);
        if (!appointments.get(position).getDoctor().getImage().equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(50)
                    .oval(true)
                    .build();
            Picasso.with(parent.getContext())
                    .load(appointments.get(position).getDoctor().getImage())
                    .transform(transformation)
                    .fit().centerCrop()
                    .into(holder.doctor_picture);
        } else {
            holder.doctor_picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.patient_placeholder));
        };
        holder.clinic_picture = (ImageView) row.findViewById(R.id.app_clinic_photo);
        if (!appointments.get(position).getClinic().getImage().equals("")) {
            Picasso.with(parent.getContext())
                    .load(appointments.get(position).getClinic().getImage())
                    .fit().centerCrop()
                    .into(holder.clinic_picture);
        } else {
            holder.clinic_picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.clinic_placeholder));
        };
        holder.specialty_name = (TextView) row.findViewById(R.id.app_specialty);
        holder.specialty_name.setText(appointments.get(position).getSpecialty().getName());
        holder.schedule_date = (TextView) row.findViewById(R.id.app_date);
        holder.schedule_date.setText(appointments.get(position).getSchedule().fullHoursString());
//        holder.schedule_time = (TextView) row.findViewById(R.id.app_time);
//        holder.schedule_time.setText(appointments.get(position).getSchedule().fullHoursString());
        holder.doctor_name = (TextView) row.findViewById(R.id.app_doctor_name);
        holder.doctor_name.setText(appointments.get(position).getDoctor().getName());
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                AppointmentPreviewFragment llf = AppointmentPreviewFragment.newInstance(appointments.get(position), PatientSessionManager.getInstance(mContext).getLoggedPatientName(), PatientSessionManager.getInstance(mContext).getLoggedPatientImage(), appointments.get(position).getSchedule());
                ft.replace(R.id.currentFragment, llf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return row;
    }
}
