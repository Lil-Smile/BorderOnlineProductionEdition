package com.lilsmile.borderonlineproductionedition.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.ads.AdView;


import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity implements Constants {



    ImageView imageWorldMap;
    ListView listViewContinents;
    Helper helper;
    AdView adView;
    Classes.Continent[] continents;

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setTitle(getString(R.string.allBorders));

        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
        //        R.layout.drawer_list_item, mScreenTitles));
        int idsDrawer[] = new int[]{R.id.imageViewIconDrawerListItem, R.id.textViewDrawerListItem};
        String[] stringsDrawer = new String[]{DRAWER_ICON, DRAWER_TEXT};

        ArrayList<HashMap<String, Object>> dataDrawer = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.allborders);
        m.put(DRAWER_TEXT, getString(R.string.allBorders));
        dataDrawer.add(m);
        m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.favorite);
        m.put(DRAWER_TEXT, getString(R.string.favorite));
        dataDrawer.add(m);
        m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.recent);
        m.put(DRAWER_TEXT, getString(R.string.recent));
        dataDrawer.add(m);
        m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.tellaboutqueue);
        m.put(DRAWER_TEXT, getString(R.string.tellAboutQueue));
        dataDrawer.add(m);
        m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.share);
        m.put(DRAWER_TEXT, getString(R.string.share));
        dataDrawer.add(m);
        m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.feedback);
        m.put(DRAWER_TEXT, getString(R.string.feedback));
        dataDrawer.add(m);
        m = new HashMap<String, Object>();
        m.put(DRAWER_ICON, R.drawable.settings);
        m.put(DRAWER_TEXT, getString(R.string.settings));
        dataDrawer.add(m);

        mDrawerList.setAdapter(new SimpleAdapter(this, dataDrawer, R.layout.drawer_list_item, stringsDrawer, idsDrawer));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);





        helper = new Helper(getApplicationContext());
        imageWorldMap = (ImageView)findViewById(R.id.imageViewWorldMap);
        listViewContinents = (ListView)findViewById(R.id.listViewContinents);

        adView = (AdView)findViewById(R.id.adViewMainActivity);
        adView.loadAd(helper.getAdRequest());

        helper.getContinents(this);




        imageWorldMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView)view);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                int pixel = bitmap.getPixel((int)motionEvent.getX(), (int)motionEvent.getY());
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                Toast.makeText(MainActivity.this, redValue+" "+greenValue+" "+blueValue, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, Borders.class);
                if ((redValue == 0)&&(greenValue==78)&&(blueValue==127))//america
                {
                    intent.putExtra(EXTRA, "America");
                } else if ((redValue == 0)&&(greenValue==98)&&(blueValue==0))//africa
                {
                    intent.putExtra(EXTRA, "Africa");
                } else if ((redValue == 1)&&(greenValue==83)&&(blueValue==126))//asia
                {
                    intent.putExtra(EXTRA, "Asia");
                } else if ((redValue == 8)&&(greenValue==89)&&(blueValue==128))//europe
                {
                    intent.putExtra(EXTRA, "Europe");
                } else if ((redValue == 132)&&(greenValue==121)&&(blueValue==125))//australia
                {
                    intent.putExtra(EXTRA, "Australia");
                }
                startActivity(intent);
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void showContinents(final Classes.Continent[] continents)
    {
        this.continents = continents;
        int[] ids = new int[]{R.id.imageViewOneContinent, R.id.textViewOneContinent};
        String[] from = new String[]{IMAGE, CONTINENT};
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Classes.Continent continent : continents)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            int imageID;
            if (continent.id==1) //europe
            {
                Log.wtf(String.valueOf(continent.id), "red");
                imageID = R.drawable.red_circle;
            } else if (continent.id == 2) //asia
            {
                Log.wtf(String.valueOf(continent.id), "blue");
                imageID = R.drawable.blue_circle;
            } else if (continent.id == 3) //africa
            {
                Log.wtf(String.valueOf(continent.id), "yellow");
                imageID = R.drawable.yellow_circle;
            } else if (continent.id == 4) //amreica
            {
                Log.wtf(String.valueOf(continent.id), "green");
                imageID = R.drawable.green_circle;
            } else //australia
            {
                Log.wtf(String.valueOf(continent.id), "purple");
                imageID = R.drawable.purple_circle;
            }
            map.put(IMAGE, imageID);
            map.put(CONTINENT, continent.name);
            data.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), data, R.layout.one_continent, from, ids);
        listViewContinents.setAdapter(simpleAdapter);
        listViewContinents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (continents[i].enable) {
                    Intent intent = new Intent(MainActivity.this, Borders.class);
                    intent.putExtra(ID, continents[i].id);
                    intent.putExtra(EXTRA, continents[i].name);
                    startActivity(intent);
                } else
                {
                    Toast.makeText(MainActivity.this, getString(R.string.notEnable), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
