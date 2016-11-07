package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.PrescriptionDetailFragment;
import com.development.unobtainium.cimedic2.models.Prescription;

import java.util.ArrayList;

/**
 * Created by kevin on 02/11/16.
 */

public class PrescriptionsListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Prescription> prescriptionsList = new ArrayList<Prescription>();

    public PrescriptionsListAdapter(Context mContext, ArrayList<Prescription> prescriptions) {
        this.mContext = mContext;
        this.prescriptionsList = prescriptions;
    }

    @Override
    public int getCount() {
        return prescriptionsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Holder {
        TextView age;
        TextView education_level;
        TextView occupation;
        TextView civil_status;
        TextView reason;
        TextView previous_history;
        TextView blood_type;
        TextView diagnosis;
        Button detail;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(mContext).inflate(R.layout.prescription_item, parent, false);
        holder.age = (TextView) row.findViewById(R.id.prescription_age);
        holder.age.setText(String.valueOf(prescriptionsList.get(i).getAge()));
        holder.occupation = (TextView) row.findViewById(R.id.prescription_occupation);
        holder.occupation.setText(prescriptionsList.get(i).getOccupation());
        holder.reason = (TextView) row.findViewById(R.id.prescription_reason);
        holder.reason.setText(prescriptionsList.get(i).getReason());
        holder.education_level = (TextView) row.findViewById(R.id.prescription_education_level);
        holder.education_level.setText(prescriptionsList.get(i).getEducation_level());
        holder.civil_status = (TextView) row.findViewById(R.id.prescription_civil_status);
        holder.civil_status.setText(prescriptionsList.get(i).getCivil_status());
        holder.previous_history = (TextView) row.findViewById(R.id.prescription_previous_history);
        holder.previous_history.setText(prescriptionsList.get(i).getReason());
        holder.blood_type = (TextView) row.findViewById(R.id.prescription_blood_type);
        holder.blood_type.setText(prescriptionsList.get(i).getBlood_type());
        holder.diagnosis = (TextView) row.findViewById(R.id.prescription_diagnosis);
        holder.diagnosis.setText(prescriptionsList.get(i).getDiagnosis());
        holder.detail = (Button) row.findViewById(R.id.prescription_detail_button);
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrescriptionDetailFragment fragment = PrescriptionDetailFragment.newInstance(prescriptionsList.get(i));
                fragment.show(((Activity) mContext).getFragmentManager(), "Dialog");
            }
        });
        return row;
    }
}
