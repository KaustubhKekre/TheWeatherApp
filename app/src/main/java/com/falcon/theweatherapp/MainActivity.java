package com.falcon.theweatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    String w;
    String img;
    double tempmax;
    double humidity;
    double latitude;
    double longitude;
    String img_url="http://openweathermap.org/img/w/";
    private String url="http://api.openweathermap.org/data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&APPID=fa4ff94bda4c3a3b60a3b30d62821f9b&units=metric";
    ImageView currentimg;
    TextView currenttemperature;
    TextView currenthumidity ;
    TextView currentweather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        currentimg=(ImageView)findViewById(R.id.currentimg);
        currenttemperature=(TextView)findViewById(R.id.temperature);
        currenthumidity =(TextView)findViewById(R.id.humidity);
        currentweather=(TextView)findViewById(R.id.weather);



        StringRequest myreq=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj1=new JSONObject(response);
                    JSONArray obj2 = obj1.getJSONArray("list");
                    JSONObject obj3 = obj2.getJSONObject(1);
                    JSONObject obj4 = obj3.getJSONObject("main");
                    tempmax = obj4.getDouble("temp_max");
                    humidity = obj4.getDouble("humidity");
                    JSONArray obj5 = obj3.getJSONArray("weather");
                    JSONObject obj6 = obj5.getJSONObject(0);
                    w = obj6.getString("description");
                    img=obj6.getString("icon");
                    Glide.with(MainActivity.this).load(img_url+img+".png").into(currentimg);
                    currentweather.setText(w.substring(0,1).toUpperCase()+w.substring(1));
                    currenthumidity.setText("Humidity="+valueOf(humidity)+"%");
                    currenttemperature.setText("Temperature="+valueOf(tempmax)+"°C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No Internet Connection.Data cannot be updated", Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        queue.add(myreq);

        TextView mumbai = findViewById(R.id.mumbai);
        mumbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  m=new Intent(MainActivity.this,mumbaiclass.class);
                startActivity(m);
            }
        });
        TextView newyork = findViewById(R.id.new_york);
        newyork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  ny=new Intent(MainActivity.this,newyorkclass.class);
                startActivity(ny);
            }
        });
        TextView dubai = findViewById(R.id.dubai);
        dubai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  d=new Intent(MainActivity.this,dubaiclass.class);
                startActivity(d);
            }
        });
    }
    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            } else {
                Toast.makeText(MainActivity.this, "Unable to find current location", Toast.LENGTH_LONG).show();

            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                getLocation();
                break;
        }
    }

}
