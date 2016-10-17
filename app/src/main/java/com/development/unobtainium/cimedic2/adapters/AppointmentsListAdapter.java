package com.development.unobtainium.cimedic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.models.Appointment;
import com.squareup.picasso.Picasso;

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
        ImageView picture;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
//        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.picture = (ImageView) row.findViewById(R.id.doctor_photo);
        if (!appointments.get(position).getPatient().getImage().equals("")) {
            Picasso.with(parent.getContext())
                    .load(appointments.get(position).getPatient().getImage())
                    .fit().centerCrop()
                    .into(holder.picture);
        } else {
            holder.picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.patient_placeholder));
        };
//        holder.name = (TextView) row.findViewById(R.id.doctor_name);
//        holder.name.setText(appointments.get(position).getName());
//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = ((Activity) mContext).getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                SchedulesFragment llf = SchedulesFragment.newInstance(dFragment.getArguments().getInt("clinic_id"), dFragment.getArguments().getInt("specialty_id"), appointments.get(position));
//                ft.replace(R.id.currentFragment, llf);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });

        return row;
    }
}
