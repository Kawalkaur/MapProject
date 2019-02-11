package com.example.mapproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String getBlock;
    LatLng sydney;
    ArrayList VillageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        requestQueue = Volley.newRequestQueue(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {

            mapFragment.getMapAsync(this);
        }
        VillageList = new ArrayList<>();


        Intent intent1 = getIntent();
        Bundle bd1 =intent1.getExtras();
        if (bd1 != null){
            String getDistrict = (String)bd1.get("districtt");
             getBlock = (String)bd1.get("blockk");
//            String getVillage = (String)bd1.get("villagee");

            Toast.makeText(this, "Address is "+getDistrict+"    "+getBlock, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        latitude();


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void latitude(){
        stringRequest = new StringRequest ( Request.Method.POST, Util.LatLong, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MapsActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                try {
                    int count =1;
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("Village");

                    for(int i =0;i<ja.length();i++)
                    {
                        JSONObject jo1 = ja.getJSONObject(i);

                        // Lat , Long columns de names aa Table ch
                        double lat = Double.parseDouble(jo1.getString("Latitude"));
                        double longg = Double.parseDouble(jo1.getString("Longitude"));
                        String vill = jo1.getString("Village");

                        // count di jagah block ya village da name aa jayega

                        sydney = new LatLng(lat, longg);
                        mMap.addMarker(new MarkerOptions().position(sydney).title(vill));

                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Some exception"+e, Toast.LENGTH_SHORT).show();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("SelectedDistrict", getBlock);
                Log.w("Inteneval",getBlock);
            return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
