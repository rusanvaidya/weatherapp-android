package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_getweatherbyName;
    EditText et_datainput;
    ListView weather_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_getweatherbyName = findViewById(R.id.getweatherbycityname);

        et_datainput = findViewById(R.id.et_datainput);
        weather_report = findViewById(R.id.weather_report);
        final WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

        btn_getweatherbyName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                weatherDataService.getCityId(et_datainput.getText().toString(), new WeatherDataService.VolleyResponseListner() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object cityId) {
                        Toast.makeText(MainActivity.this, "MAIN ID : " + cityId, Toast.LENGTH_SHORT).show();
                        weatherDataService.getForecastByName(""+cityId, new WeatherDataService.ForecastByIDResponse() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherReportModel> weatherReportModels) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
                                weather_report.setAdapter(arrayAdapter);
                            }
                        });
                    }
                });
            }
        });
    }
}