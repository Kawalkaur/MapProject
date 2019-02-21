package com.example.mapproject;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String  getDistrict, getBlock;
    TextView name1,name2;
    Dialog dialog;
    int idval;
    String snippet;
    int idd;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog adialog;
    LatLng sydney;
    List<String> myList = new ArrayList<>();

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

        Intent intent1 = getIntent();
        Bundle bd1 =intent1.getExtras();
        if (bd1 != null){
            getDistrict = (String)bd1.get("districtt");
            getBlock = (String)bd1.get("blockk");
            //Toast.makeText(this, "Address is "+getDistrict+"    "+getBlock, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        //   mMap.setOnInfoWindowClickListener(this);
        latitude();
       /* googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                String phone ="123654789" ;
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                if (ActivityCompat.checkSelfPermission( MapsActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(callIntent);
            }
        });*/

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void latitude(){
        stringRequest = new StringRequest ( Request.Method.POST, Util.LatLong, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(MapsActivity.this, ""+response, Toast.LENGTH_SHORT).show();

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
                        String vill = jo1.getString("VILLAGE");
                         idd = jo1.getInt("ID");
//                        String name = jo1.getString("NAME");
//                        String phone = jo1.getString("PHONE");
//                        String designation = jo1.getString("DESIGNATION");
//                        String department = jo1.getString("DEPARTMENT");
//                        String snippet  = "Name: "+name + "," +
//                                "phone: "+phone + "," +
//                                "Designation: "+designation +"," +
//                                "Department: "+department + ",";
                        // count di jagah block ya village da name aa jayega

                        sydney = new LatLng(lat, longg);
                        mMap.addMarker(new MarkerOptions().position(sydney).title(vill+idd)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                        // Custom_info_window adapter = new Custom_info_window(MapsActivity.this);

                    }


                    // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,12), 4000,null);


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


    @Override
    public void onInfoWindowClick(Marker marker) {

        idval = Integer.parseInt(marker.getTitle().replaceAll("[^0-9]", ""));
        getData();
        //Toast.makeText(this, ""+marker.getTitle().replaceAll("[^0-9]", ""), Toast.LENGTH_SHORT).show();

     //  showDialouge();

    }



    void AlertDialog()
    {


    }

    void getData () {
        stringRequest = new StringRequest( Request.Method.POST, Util.getdetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(MapsActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("Village");
                    myList.clear();

                    for(int i =0;i<ja.length();i++)
                    {
                        JSONObject jo1 = ja.getJSONObject(i);

                        String name = jo1.getString("Name");
                        String phone = jo1.getString("Phone");
                        String designation = jo1.getString("Designation");
                        String department = jo1.getString("Department");
                        snippet  = "Name: "+name + "," +
                                "phone: "+phone + "," +
                                "Designation: "+designation +"," +
                                "Department: "+department + "\n\n";

                        myList.add(snippet);

                    }

                    dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                    View view = getLayoutInflater().inflate(R.layout.print_count_dialouge, null);
                    TextView namet1 = view.findViewById(R.id.name1);
                    namet1.setText(myList.toString().trim());
                    dialogBuilder.setView(view);
                    adialog = dialogBuilder.create();
                    adialog.show();


                    Log.w("AllDATA",myList.toString());



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Some exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> mapi = new HashMap<>();
                mapi.put("idd", String.valueOf(idval));
                Log.w("IDVAL", String.valueOf(idval));
                return mapi;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}