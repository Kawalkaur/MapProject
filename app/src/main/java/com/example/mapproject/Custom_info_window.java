package com.example.mapproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Custom_info_window implements GoogleMap.InfoWindowAdapter {
    private Activity context;

    public Custom_info_window(Activity context,int rating){
        this.context = context;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.infowindow,null);
        TextView tvTitle =view.findViewById(R.id.titleInfo);
        TextView tvSnippet = view.findViewById(R.id.snippetInfo);
        tvTitle.setText(marker.getTitle());
        tvSnippet.setText(marker.getSnippet());
        return view;
    }
}
