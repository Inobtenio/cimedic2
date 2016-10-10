package com.development.unobtainium.cimedic2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.models.Doctor;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by unobtainium on 9/10/16.
 */
public class DoctorsListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Doctor> doctorsList = new ArrayList<Doctor>();

    public DoctorsListAdapter(Context mContext, ArrayList<Doctor> specialtiesList) {
        this.mContext = mContext;
        this.doctorsList = specialtiesList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View cell;
        cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
//        String loggedPatientId = PatientSessionManager.getInstance(parent.getContext()).getLoggedPatientId();
        holder.picture = (ImageView) cell.findViewById(R.id.doctor_photo);
        if (!doctorsList.get(position).getImage().equals("")) {
            Picasso.with(parent.getContext())
                    .load(doctorsList.get(position).getImage())
                    .fit().centerCrop()
                    .into(holder.picture);
        } else {
            holder.picture.setImageDrawable(parent.getResources().getDrawable(R.drawable.patient_placeholder));
        };
        holder.name = (TextView) cell.findViewById(R.id.doctor_name);
        holder.name.setText(doctorsList.get(position).getName());

        return cell;
    }
}
