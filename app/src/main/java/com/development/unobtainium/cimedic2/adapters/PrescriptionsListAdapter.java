package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        TextView reason;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(mContext).inflate(R.layout.prescription_item, parent, false);
        holder.reason = (TextView) row.findViewById(R.id.prescription_reason);
        holder.reason.setText(prescriptionsList.get(i).getReason());
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrescriptionDetailFragment fragment = PrescriptionDetailFragment.newInstance();
                fragment.show(((Activity) mContext).getFragmentManager(), "Dialog");
            }
        });
        return row;
    }
}
