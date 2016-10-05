package com.development.unobtainium.cimedic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.models.Specialty;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by unobtainium on 2/10/16.
 */
public class SpecialtiesTableAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Specialty> specialtiesList = new ArrayList<Specialty>();

    public SpecialtiesTableAdapter(Context mContext, ArrayList<Specialty> specialtiesList) {
        this.mContext = mContext;
        this.specialtiesList = specialtiesList;
    }

    @Override
    public int getCount() {
        return specialtiesList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View cell;
        cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
//        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.picture = (ImageView) cell.findViewById(R.id.specialty_photo);
        if (!specialtiesList.get(position).getImage().equals("")) {
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(60)
                    .oval(true)
                    .build();
            Picasso.with(parent.getContext())
                    .load(specialtiesList.get(position).getImage())
                    .fit().centerCrop()
                    .transform(transformation)
                    .into(holder.picture);
        } else {
            holder.picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.clinic_placeholder));
        };
        return cell;
    }
}
