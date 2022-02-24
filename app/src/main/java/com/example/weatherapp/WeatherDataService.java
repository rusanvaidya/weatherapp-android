package com.example.weatherapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String LOCATION_SEARCH_QUERY = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_BY_ID = "https://www.metaweather.com/api/location/";

    Context context;
    String cityId;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListner{
        void onError(String message);

        void onResponse(Object cityId);
    }

    public String getCityId(String cityName, VolleyResponseListner volleyResponseListner){
        String url = LOCATION_SEARCH_QUERY + cityName;
        cityId ="";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject cityinfo = response.getJSONObject(0);
                    cityId = cityinfo.getString("woeid");
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
//                Toast.makeText(context, cityId, Toast.LENGTH_SHORT).show();
                volleyResponseListner.onResponse(cityId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                volleyResponseListner.onError("SOMETHING WENT WRONG");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
        return cityId;
    }

    public interface ForecastByIDResponse{
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getForecastByName(String cityId, ForecastByIDResponse forecastByIDResponse) {

        List<WeatherReportModel> weatherreportmodels = new ArrayList<>();

        String url = QUERY_FOR_CITY_BY_ID + cityId;
        cityId = "";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray weather_list = response.getJSONArray("consolidated_weather");

                    for(int i=0; i<weather_list.length(); i++) {
                        WeatherReportModel One_day = new WeatherReportModel();

                        JSONObject first_day_api = (JSONObject) weather_list.get(i);
                        One_day.setId(first_day_api.getInt("id"));
                        One_day.setWeather_state_name(first_day_api.getString("weather_state_name"));
                        One_day.setWeather_state_abbr(first_day_api.getString("weather_state_abbr"));
                        One_day.setCreated(first_day_api.getString("created"));
                        One_day.setApplicable_date(first_day_api.getString("applicable_date"));
                        One_day.setWind_direction_compass(first_day_api.getString("wind_direction_compass"));
                        One_day.setMin_temp(first_day_api.getLong("min_temp"));
                        One_day.setMax_temp(first_day_api.getLong("max_temp"));
                        One_day.setThe_temp(first_day_api.getLong("the_temp"));
                        One_day.setWind_speed(first_day_api.getLong("wind_speed"));
                        One_day.setWind_direction(first_day_api.getLong("wind_direction"));
                        One_day.setVisibility(first_day_api.getLong("visibility"));
                        One_day.setHumidity(first_day_api.getInt("humidity"));
                        One_day.setAir_pressure(first_day_api.getInt("air_pressure"));
                        One_day.setPredictability(first_day_api.getInt("predictability"));
                        weatherreportmodels.add(One_day);
                    }
                    forecastByIDResponse.onResponse(weatherreportmodels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
//                    ForecastByIDResponse.onError("SOMETHING WENT WRONG");
                }
            });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
