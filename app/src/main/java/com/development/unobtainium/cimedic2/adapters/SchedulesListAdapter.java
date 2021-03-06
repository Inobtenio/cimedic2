package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.AppointmentPreviewFragment;
import com.development.unobtainium.cimedic2.fragments.SchedulesFragment;
import com.development.unobtainium.cimedic2.managers.PatientSessionManager;
import com.development.unobtainium.cimedic2.models.Doctor;
import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.models.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kevin on 10/11/16.
 */
public class SchedulesListAdapter extends BaseAdapter {
    private Context mContext;
    private SchedulesFragment sFragment;
    private ArrayList<Schedule> schedulesList = new ArrayList<Schedule>();

    public SchedulesListAdapter(Context mContext, ArrayList<Schedule> schedulesList, SchedulesFragment fragment) {
        this.mContext = mContext;
//        this.dFragment = fragment;
//        if (schedulesList.size() > 1) {
//            Collections.sort(schedulesList, new Comparator<Schedule>() {
//                public int compare(Schedule sch1, Schedule sch2) {
//                    // ## Ascending order
//                    return Integer.getInteger(sch1.getStart()) - Integer.getInteger(sch2.getStart());
//                }
//            });
//        }
        this.sFragment = fragment;
        this.schedulesList = schedulesList;
    }

    @Override
    public int getCount() {
        return schedulesList.size();
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
        TextView text;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
//        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.text = (TextView) row.findViewById(R.id.scheduleText);
        holder.text.setText(schedulesList.get(position).hoursString());
        if (schedulesList.get(position).taken){
            row.setBackground(parent.getResources().getDrawable(R.drawable.red_rounded_text_view));
            holder.text.setTextColor(Color.WHITE);
        } else {
            row.setBackground(parent.getResources().getDrawable(R.drawable.rounded_text_view));
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (schedulesList.get(position).taken){
                    Toast.makeText(mContext, "Este horario ya ha sido reservado", Toast.LENGTH_SHORT).show();
                } else {
                    FragmentManager fm = ((Activity) mContext).getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    AppointmentPreviewFragment llf = AppointmentPreviewFragment.newInstance(sFragment, PatientSessionManager.getInstance(mContext).getLoggedPatientName(), PatientSessionManager.getInstance(mContext).getLoggedPatientImage(), schedulesList.get(position));
                    ft.replace(R.id.currentFragment, llf);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        return row;
    }
}
