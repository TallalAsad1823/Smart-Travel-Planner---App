package com.example.smarttravelplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class PlacesAdapter extends ArrayAdapter<PlaceModel> {

    public PlacesAdapter(Context context, ArrayList<PlaceModel> places) {
        super(context, 0, places);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlaceModel place = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_place, parent, false);
        }


        TextView tvName = convertView.findViewById(R.id.textTitle);
        TextView tvDesc = convertView.findViewById(R.id.textDesc);


        if (place != null) {
            tvName.setText(place.getName());
            tvDesc.setText(place.getDescription());
        }

        return convertView;
    }
}