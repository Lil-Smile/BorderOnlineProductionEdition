package com.lilsmile.borderonlineproductionedition.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Smile on 12.01.16.
 */
public class Helper { //this class works with preferences and site's API

    Retrofit retrofit;
    BorderOnlineApi api;

    public AdRequest getAdRequest() {
        return adRequest;
    }

    private AdRequest adRequest;

    DBHelper dbHelper;

    public void getContinents(final MainActivity activity) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(false, "continents", null, null, null, null, null, null, null);

        if (c.moveToFirst())
        {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int enableColIndex = c.getColumnIndex("enable");

            Classes.Continent[] array = new Classes.Continent[c.getCount()];
            int i = 0;
            do{
                Classes.Continent continent = new Classes().new Continent();
                continent.name = c.getString(nameColIndex);
                int enable = c.getInt(enableColIndex);
                continent.enable = (enable==1) ? true : false;
                continent.id = c.getInt(idColIndex);
                array[i] = continent;
                i++;

            } while (c.moveToNext());
            activity.showContinents(array);
        } else {
            if (isOnline()) {
                Call<JsonObject> call = api.getContinents();
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.wtf("code", String.valueOf(response.code()));
                        Log.wtf("continents", response.body().toString());

                        Gson gson = new Gson();
                        JsonArray jsonArray = response.body().getAsJsonArray("data");
                        Classes.Continent[] array = new Classes.Continent[jsonArray.size()];
                        Type listType = new TypeToken<List<Classes.Continent>>() {
                        }.getType();
                        List<Classes.Continent> continents = gson.fromJson(jsonArray.toString(), listType);

                        for (int i = 0; i < continents.size(); i++) {
                            array[i] = continents.get(i);
                        }
                        dbHelper.addContinents(array);
                        activity.showContinents(array);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.wtf("continents", "fail");
                    }
                });
            } else
            {
                Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void getBorders(final Borders context, final int continentID)
    {
        Classes.Border[] borders = dbHelper.getBorders(continentID);
        if (borders != null)
        {
            context.showBorders(borders, "");
            return;
        }
        if (isOnline()) {
            Call<JsonObject> call = api.getBorders(continentID);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.wtf("code", String.valueOf(response.code()));
                    Log.wtf("borders", response.body().toString());

                    Gson gson = new Gson();
                    JsonArray jsonArray = response.body().getAsJsonArray("data");
                    Classes.Border[] array = new Classes.Border[jsonArray.size()];
                    Type listType = new TypeToken<List<Classes.Border>>() {
                    }.getType();
                    List<Classes.Border> borders = gson.fromJson(jsonArray.toString(), listType);
                    for (int i = 0; i < borders.size(); i++) {
                        array[i] = borders.get(i);
                    }
                    if (dbHelper.getBorders(continentID) == null) {
                        dbHelper.addBorders(array, continentID);
                    } else {
                        dbHelper.changeBorders(array, continentID);
                    }
                    context.showBorders(array, "");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.wtf("borders", "fail");
                }
            });
        } else
        {
            Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }

    public void getTransits(final Transits context, int id, String direction)
    {
        Classes.Checkpoint[] checkpoints = dbHelper.getCheckpoints(id);
        if (checkpoints!=null)
        {
            context.fillTableLayout(checkpoints);
            return;
        }
        if (isOnline()) {
            Call<JsonObject> call = api.getCheckpoints(id, direction);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.wtf("checkpoints", response.body().toString());
                    Gson gson = new Gson();
                    JsonArray jsonArray = response.body().getAsJsonArray("data");
                    Classes.Checkpoint[] array = new Classes.Checkpoint[jsonArray.size()];
                    Type listType = new TypeToken<List<Classes.Checkpoint>>() {
                    }.getType();
                    List<Classes.Checkpoint> checkpoints = gson.fromJson(jsonArray.toString(), listType);
                    for (int i = 0; i < checkpoints.size(); i++) {
                        array[i] = checkpoints.get(i);
                    }
                    context.fillTableLayout(array);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    }





    Context context;
    private static SharedPreferences sharedPreferences;

    Helper(Context context)
    {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        adRequest = new AdRequest.Builder().build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://104.236.67.176:8080/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(BorderOnlineApi.class);

        dbHelper = new DBHelper(context);
    }

    public String[] getLinks() {
        return links;
    }

    public LineGraphSeries<DataPoint> getSystemSeries() //todo add here id/name of checkpoint
    {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        return series;
    }
    public LineGraphSeries<DataPoint> getUsersSeries()
    {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 4),
                new DataPoint(2, 8),
                new DataPoint(3, 1),
                new DataPoint(4, 5)
        });
        return series;
    }


    public void getInfoAboutBorder(final Transits context, int id)
    {
        if(isOnline()) {
            Call<JsonObject> call = api.getBorderInfo(id);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.wtf("info", response.body().toString());
                    Gson gson = new Gson();
                    JsonObject jsonObject = response.body().getAsJsonObject("data");
                    Classes.BorderInfo borderInfo = gson.fromJson(jsonObject, Classes.BorderInfo.class);
                    context.setInfoAboutBorder(borderInfo.info);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } else
        {
            Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }
    }




    private String[] links = new String[]{"http://www.google.com", "http://www.facebook.com", "http://www.vk.com", "http://www.instagram.com", "http://www.twitter.com", "http://www.periscope.com" };

    public void getInfoAboutCheckpoint(final TransitInfo context, int id) {
        if (isOnline())
        {
            Call<JsonObject> call = api.getCheckpointInfo(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.wtf("checkpointINFO", response.body().toString());
                Gson gson = new Gson();
                JsonObject jsonObject = response.body().getAsJsonObject("data");
                Classes.CheckpointInfo checkpointInfo = gson.fromJson(jsonObject, Classes.CheckpointInfo.class);
                context.setInfo(checkpointInfo);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    } else
        {
            Toast.makeText(context, context.getString(R.string.noInternet), Toast.LENGTH_LONG).show();
        }

    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void addFavoriteBorder(int id)
    {
        dbHelper.addFavoriteBorder(id);
    }

    public void delFavoriteBorder(int id)
    {
        dbHelper.delFavoriteBorder(id);
    }

    public boolean isFavoriteBorder(int id)
    {
        return dbHelper.isFavoriteBorder(id);
    }






}
