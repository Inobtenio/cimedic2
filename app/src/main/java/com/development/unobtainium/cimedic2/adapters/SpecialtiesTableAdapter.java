package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.DoctorsFragment;
import com.development.unobtainium.cimedic2.fragments.SpecialtiesFragment;
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
    private SpecialtiesFragment sFragment;
    private ArrayList<Specialty> specialtiesList = new ArrayList<Specialty>();

    public SpecialtiesTableAdapter(Context context, SpecialtiesFragment fragment, ArrayList<Specialty> specialtiesList) {
        this.mContext = context;
        this.sFragment = fragment;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialty_item, parent, false);
        holder.picture = (ImageView) row.findViewById(R.id.specialty_photo);
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
        holder.name = (TextView) row.findViewById(R.id.specialty_name);
        holder.name.setText(specialtiesList.get(position).getName());
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Log.e("SPECIALTY ID", String.valueOf(specialtiesList.get(position).getId()));
                DoctorsFragment llf = DoctorsFragment.newInstance(sFragment.getArguments().getInt("clinic_id"), specialtiesList.get(position).getId());
                ft.replace(R.id.currentFragment, llf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return row;
    }
}
