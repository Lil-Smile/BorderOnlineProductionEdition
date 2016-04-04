package com.lilsmile.borderonlineproductionedition.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.jjoe64.graphview.GraphView;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TransitInfo extends ActionBarActivity implements Constants, View.OnClickListener{

    TextView textViewInfo, textViewTime, textViewQueue, textViewCityes;
    GraphView graphViewUsers, graphViewSystem;
    FloatingActionButton fab;
    TableLayout tableLayoutAdditionalInfo;

    String currentKind = CARS;


    Helper helper;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    Classes.CheckpointInfo checkpointInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_info);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_transit_info);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_transit_info);

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



    
        helper = new Helper(this);


        textViewInfo = (TextView)findViewById(R.id.textViewTransitInfoCities);
        textViewQueue = (TextView)findViewById(R.id.textViewTransitInfoQueue);
        textViewTime = (TextView)findViewById(R.id.textViewTransitInfoTime);
        textViewCityes = (TextView) findViewById(R.id.textViewCityes);

        textViewInfo.setText(getIntent().getStringExtra(INFO)); //todo change setting info
        textViewTime.setText("18 h 10 m");
        textViewQueue.setText("20 "+currentKind);
        textViewCityes.setText(getIntent().getStringExtra(EXTRA));

        graphViewSystem = (GraphView)findViewById(R.id.graphViewSystem);
        graphViewUsers = (GraphView)findViewById(R.id.graphViewUsers);

        graphViewUsers.addSeries(helper.getUsersSeries());
        graphViewSystem.addSeries(helper.getSystemSeries());

        fab = (FloatingActionButton)findViewById(R.id.fab);
        ObservableScrollView scrollView = (ObservableScrollView)findViewById(R.id.scrollViewTransitInfo);
        fab.attachToScrollView(scrollView);

        fab.setOnClickListener(this);

        setTitle(getIntent().getStringExtra(BORDER));
        int id = getIntent().getIntExtra(ID,0);


        tableLayoutAdditionalInfo = (TableLayout)findViewById(R.id.tableLayoutAdditionalInfo);
        final LayoutInflater inflater = getLayoutInflater();
        String[] links = helper.getLinks();
        for (String link: links)
        {
            View view = inflater.inflate(R.layout.one_add_info,null);
            final TextView textView = (TextView) view.findViewById(R.id.textViewOneAddInfo);
            textView.setText(link);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView1 = (TextView) view.findViewById(R.id.textViewOneAddInfo);
                    String url = textView1.getText().toString();
                    Intent intent = new Intent(TransitInfo.this, CameraShow.class);
                    intent.putExtra(URL, url);
                    startActivity(intent);
                }
            });
            tableLayoutAdditionalInfo.addView(view);
            View breaker = new View(this);
            breaker.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.breakerWidth)));
            breaker.setBackgroundColor(Color.parseColor("#ff121212"));
            tableLayoutAdditionalInfo.addView(breaker);

        }

        helper.getInfoAboutCheckpoint(this, id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transit_info, menu);
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
            startActivity(new Intent(TransitInfo.this, SettingsActivity.class));
            return true;
        }


        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_cars_info:
            {
                currentKind = CARS;
                setInfo(checkpointInfo);
                break;
            }
            case R.id.action_trucks_info:
            {
                currentKind = TRUCKS;
                setInfo(checkpointInfo);
                break;
            }
            case R.id.action_busses_info:
            {
                currentKind = BUSSES;
                setInfo(checkpointInfo);
                break;
            }
            case R.id.action_favorite:
            {
                break;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fab:
            {
                Intent intent = new Intent(TransitInfo.this, Chat.class);
                startActivity(intent);
                break;
            }
        }
    }

    public void setInfo(Classes.CheckpointInfo checkpointInfo)
    {
        this.checkpointInfo = checkpointInfo;
        textViewInfo = (TextView)findViewById(R.id.textViewTransitInfoCities);
        textViewQueue = (TextView)findViewById(R.id.textViewTransitInfoQueue);
        textViewTime = (TextView)findViewById(R.id.textViewTransitInfoTime);

        textViewInfo.setText(checkpointInfo.description);
        if (currentKind.equals(CARS)) {
            textViewQueue.setText(checkpointInfo.by_users_car_queue_length+" "+CARS);
            textViewTime.setText(checkpointInfo.by_users_car_queue_time+"");
        } else if (currentKind.equals(BUSSES))
        {
            textViewQueue.setText(checkpointInfo.by_users_bus_queue_length+" "+BUSSES);
            textViewTime.setText(checkpointInfo.by_users_bus_queue_time+"");
        } else {
            textViewQueue.setText(checkpointInfo.by_users_truck_queue_length+" "+TRUCKS);
            textViewTime.setText(checkpointInfo.by_users_truck_queue_time+"");
        }
    }



}
