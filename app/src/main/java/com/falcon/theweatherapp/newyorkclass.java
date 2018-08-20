package com.falcon.theweatherapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class newyorkclass extends AppCompatActivity {
    int count;
    String[] w;
    String[] img;
    String[] d;
    double[] tempmin;
    double[] tempmax;
    double[] humidity;







    private static final String url = "http://api.openweathermap.org/data/2.5/forecast?id=5106292&APPID=fa4ff94bda4c3a3b60a3b30d62821f9b&units=metric";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newyork);
        final ProgressBar pbar=(ProgressBar)findViewById(R.id.progressbar);
        pbar.setVisibility(View.VISIBLE);

        StringRequest m = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {JSONObject obj1 = new JSONObject(response);
                    count=obj1.getInt("cnt");
                    w=new String[count];
                    img=new String[count];
                    d=new String[count];
                    tempmin=new double[count];
                    tempmax=new double[count];
                    humidity=new double[count];

                    JSONArray obj2 = obj1.getJSONArray("list");
                    for(int i=0;i<count;i++) {
                        JSONObject obj3 = obj2.getJSONObject(i);
                        d[i] = obj3.getString("dt_txt");
                        JSONObject obj4 = obj3.getJSONObject("main");
                        tempmin[i] = obj4.getDouble("temp_min");
                        tempmax[i] = obj4.getDouble("temp_max");
                        humidity[i] = obj4.getDouble("humidity");
                        JSONArray obj5 = obj3.getJSONArray("weather");
                        JSONObject obj6 = obj5.getJSONObject(0);
                        w[i] = obj6.getString("description");
                        img[i]=obj6.getString("icon");
                    }
                    RecyclerView recyclernewyork = (RecyclerView) findViewById(R.id.recyclernewyork);
                    recyclernewyork.setLayoutManager(new LinearLayoutManager(newyorkclass.this));
                    pbar.setVisibility(View.GONE);
                    recyclernewyork.setAdapter(new weatheradapter(w, d, tempmin, tempmax, humidity,img));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbar.setVisibility(View.GONE);
                Toast.makeText(newyorkclass.this, "No Internet Connection.Data cannot be updated", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(newyorkclass.this);
        queue.add(m);

    }
}
