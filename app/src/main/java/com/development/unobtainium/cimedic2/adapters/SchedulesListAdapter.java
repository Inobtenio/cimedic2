package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.SchedulesFragment;
import com.development.unobtainium.cimedic2.models.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kevin on 10/11/16.
 */
public class SchedulesListAdapter extends BaseAdapter {
    private Context mContext;
    private SchedulesFragment dFragment;
    private ArrayList<Schedule> schedulesList = new ArrayList<Schedule>();

    public SchedulesListAdapter(Context mContext, ArrayList<Schedule> schedulesList) {
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
            row.setBackgroundColor(Color.parseColor("#FB0D1B"));
            holder.text.setTextColor(Color.WHITE);
        }
//        row.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = ((Activity) mContext).getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                SchedulesFragment llf = SchedulesFragment.newInstance(dFragment.getArguments().getInt("clinic_id"), dFragment.getArguments().getInt("specialty_id"), schedulesList.get(position).getId());
//                ft.replace(R.id.currentFragment, llf);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });

        return row;
    }
}
