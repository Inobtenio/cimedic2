package com.development.unobtainium.cimedic2.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.development.unobtainium.cimedic2.R;
import com.development.unobtainium.cimedic2.fragments.SpecialtiesFragment;
import com.development.unobtainium.cimedic2.models.Clinic;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by unobtainium on 25/09/16.
 */
public class ClinicsListAdapter extends BaseAdapter {
    private ArrayList<Clinic> clinics = new ArrayList<Clinic>();
    private Context mContext;

    public ClinicsListAdapter(Context mContext, ArrayList<Clinic> clinics) {
        this.clinics = clinics;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return clinics.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class Holder{
        ImageView picture;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = LayoutInflater.from(parent.getContext()).inflate(R.layout.clinic_item, parent, false);
        holder.picture = (ImageView) row.findViewById(R.id.clinic_picture);
        if (holder.picture == null) {
            holder.picture = new ImageView(mContext);
        }
        Picasso.with(mContext).load(clinics.get(position).getImage()).resize(1200,280).into(holder.picture);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((Activity) mContext).getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SpecialtiesFragment llf = SpecialtiesFragment.newInstance(clinics.get(position).getId());
                ft.replace(R.id.currentFragment, llf);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return row;
    }

}
