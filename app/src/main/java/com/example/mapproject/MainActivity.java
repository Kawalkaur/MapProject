package com.example.mapproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
    };
    Spinner District, Block, Village;
    ArrayList DistrictList, BlockList, VillageList;
    String SelectedDistrict, SelectedBlock, SelectedVillage;
    Button Next;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            if(!hasPermissions(this, PERMISSIONS)){
                //   Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
            }
        }


        District = findViewById(R.id.district);
        Block = findViewById(R.id.block);
        // Village = findViewById(R.id.village);
        DistrictList =new ArrayList<>();
        DistrictList.add("--Select District--");
        District.setAdapter(new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item, DistrictList));
        BlockList = new ArrayList<>();
        BlockList.add("--Select Block--");
        Block.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item));
       /* VillageList = new ArrayList<>();
        VillageList.add("--Select Village--");
        Village.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item));*/
        Next = findViewById(R.id.next);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("districtt",SelectedDistrict);
                intent.putExtra("blockk",SelectedBlock);
                //    intent.putExtra("villagee",SelectedVillage);
                startActivity(intent);
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        getDistrict();
        District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedDistrict =District.getItemAtPosition(District.getSelectedItemPosition()).toString();
                BlockList.clear();
                BlockList.add("--Select Block--");
                getBlocks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedBlock = Block.getItemAtPosition(Block.getSelectedItemPosition()).toString();
//                VillageList.clear();
//                VillageList.add("--Select Village--");
                //getVillage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



     /*   Village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedVillage = Village.getItemAtPosition(Village.getSelectedItemPosition()).toString();
//                LatitudeList.clear();
//                LongitudeList.clear();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
    }
    void getDistrict() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.Districts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray ja = new JSONArray(response);
                    if(ja.length()>0) {

                        int k = ja.length();


                        for (int i = 0; i < k; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            DistrictList.add(jo.getString("District"));
                            District.setAdapter(new ArrayAdapter<String>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item, DistrictList));

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Some exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(MainActivity.this, "Some error", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);

    }

    // < Code to get Blocks

    void getBlocks() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.Blocks, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja = new JSONArray(response);
                    if(ja.length()>0) {
                        int k = ja.length();


                        for (int i = 0; i < k; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            BlockList.add(jo.getString("Block"));
                            Block.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, BlockList));

                        }
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("SelectedDistrict", SelectedDistrict);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
    // get blocks method end here


    public  boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int count=0;
        if (requestCode == PERMISSION_ALL) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //  onPPSButtonPress();
                    } else {
                        count++;
                        //  Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
                        //requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_CODE);
                    }
                }
                else if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //  onPPSButtonPress();
                    } else {
                        //  Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
                        //requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_CODE);
                        count++;
                    }
                }

                else if(permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //  onPPSButtonPress();
                    } else {
                        //   Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
                        //requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_CODE);
                        count++;
                    }
                }
                else if(permission.equals(Manifest.permission.CAMERA)){
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //  onPPSButtonPress();
                    } else {
                        count++;
                        //    Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
                        //requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_CODE);
                    }

                }





                else if (permission.equals(Manifest.permission.READ_PHONE_STATE)){
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //  onPPSButtonPress();
                    } else {
                        //  Toast.makeText(getApplicationContext(),"Permissions denied .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
                        //requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_CODE);
                    }
                }}
            if(count>=1){
                Toast.makeText(getApplicationContext(),"One or ALL Permissions are  denied by you .You can change them in Settings>Apps",Toast.LENGTH_LONG).show();
            }

        }


    }

}
