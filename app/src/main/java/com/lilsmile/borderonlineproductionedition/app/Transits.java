package com.lilsmile.borderonlineproductionedition.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Transits extends ActionBarActivity implements Constants{

    AdView adView;

    TableLayout tableLayout;

    Helper helper;

    int borderId;


    String currentKind = CARS;

    Classes.Checkpoint[] checkpoints;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transits);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_transits);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_transits);

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



        String title = getIntent().getStringExtra(EXTRA);
        setTitle(title);
        borderId = getIntent().getIntExtra(ID,0);
        int id = getIntent().getIntExtra(ID,0);
        String direction = getIntent().getStringExtra(DIRECTION);

        helper = new Helper(this);
        Log.wtf("id+direction", id+" "+direction);
        helper.getTransits(this, id, direction);

        helper.getInfoAboutBorder(this, id);
    }

    public void fillTableLayout(final Classes.Checkpoint[] checkpoints)
    {
        this.checkpoints = checkpoints;
        tableLayout = (TableLayout)findViewById(R.id.tableLayoutTransits);
        tableLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();
        int count = 0;
        for (final Classes.Checkpoint checkpoint : checkpoints)
        {

            View view = inflater.inflate(R.layout.one_transit, null);
            view.setTag(count);
            count++;
            TextView cityName1View, cityName2View, queueView, dataSource;
            ImageView colorCheckingView;
            cityName1View = (TextView)view.findViewById(R.id.textViewTransitCityName1);
            cityName2View = (TextView)view.findViewById(R.id.textViewTransitCityName2);
            queueView = (TextView)view.findViewById(R.id.textViewQueue);
            dataSource = (TextView) view.findViewById(R.id.textViewDataSource);
            colorCheckingView = (ImageView) view.findViewById(R.id.imageViewColorChecking);
            cityName1View.setText(checkpoint.backward_locality);
            cityName2View.setText(checkpoint.forward_locality);
            String queueString;
            String dataString;
            int color = 0;
            if (currentKind.equals(CARS))
            {
                if (checkpoint.queue.by_users.car != -1)
                {
                    queueString = checkpoint.queue.by_users.car+" minutes";
                    if (checkpoint.queue.by_users.car <30)
                    {
                        color = R.color.green;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_circle));
                    } else
                    {
                        color = R.color.red;
                    }
                    dataString = getString(R.string.fromUsers);
                } else if (checkpoint.queue.official.car!=-1)
                {
                    queueString = checkpoint.queue.official.car+" minutes";
                    dataString = getString(R.string.fromSite);
                    if (checkpoint.queue.official.car <30)
                    {
                        color = R.color.green;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_circle));
                    } else
                    {
                        color = R.color.red;
                    }
                } else
                {
                    queueString = getString(R.string.noData);
                    color = R.color.grey;
                    dataString = getString(R.string.nowhere);
                    colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.purple_circle));
                }
            } else if (currentKind.equals(TRUCKS))
            {
                if (checkpoint.queue.by_users.truck != -1) {
                    queueString = checkpoint.queue.by_users.truck + " minutes";
                    dataString = getString(R.string.fromUsers);
                    if (checkpoint.queue.by_users.truck <30)
                    {
                        color = R.color.green;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_circle));
                    } else
                    {
                        color = R.color.red;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_circle));
                    }
                } else if (checkpoint.queue.official.truck!=-1)
                {
                    queueString = checkpoint.queue.official.truck + " minutes";
                    dataString = getString(R.string.fromSite);
                    if (checkpoint.queue.official.truck <30)
                    {
                        color = R.color.green;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_circle));
                    } else
                    {
                        color = R.color.red;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_circle));
                    }
                }else
                {
                    queueString = getString(R.string.noData);
                    color = R.color.grey;
                    dataString = getString(R.string.nowhere);
                    colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.purple_circle));
                }
            } else
            {
                if (checkpoint.queue.by_users.bus!=-1) {
                    queueString = checkpoint.queue.by_users.bus + " minutes";
                    dataString = getString(R.string.fromUsers);
                    if (checkpoint.queue.by_users.bus<30)
                    {
                        color = R.color.green;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_circle));
                    } else
                    {
                        color = R.color.red;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_circle));
                    }
                }else if (checkpoint.queue.official.bus != -1)
                {
                    queueString = checkpoint.queue.official.bus + " minutes";
                    dataString = getString(R.string.fromSite);
                    if (checkpoint.queue.official.bus<30)
                    {
                        color = R.color.green;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_circle));
                    } else
                    {
                        color = R.color.red;
                        colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_circle));
                    }
                } else
                {
                    queueString = getString(R.string.noData);
                    color = R.color.grey;
                    dataString = getString(R.string.nowhere);
                    colorCheckingView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.purple_circle));
                }
            }
            queueView.setText(queueString);
            dataSource.setText(dataString);
            queueView.setTextColor(getResources().getColor(color));
            TableRow tableRow = new TableRow(this);
            tableRow.addView(view);
            tableLayout.addView(tableRow);
            View breaker = new View(this);
            breaker.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.breakerWidth)));
            breaker.setBackgroundColor(Color.parseColor("#ff121212"));
            tableLayout.addView(breaker);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Transits.this, TransitInfo.class);
                    int count = (Integer)view.getTag();
                    intent.putExtra(EXTRA, checkpoints[count].forward_locality+"-"+checkpoints[count].backward_locality);
                    intent.putExtra(ID, checkpoints[count].id);
                    intent.putExtra(BORDER, getTitle());
                    TextView textViewInfo = (TextView) findViewById(R.id.textViewInfoBorder);
                    intent.putExtra(INFO, textViewInfo.getText().toString() );
                    startActivity(intent);
                }
            });



        }
        if (helper.isFavoriteBorder(borderId))
        {
            ActionMenuItemView fav = (ActionMenuItemView) findViewById(R.id.action_favorite);
            fav.setIcon(getResources().getDrawable(R.drawable.ic_favorite_outline_black_24dp));
        } else
        {
            ActionMenuItemView fav = (ActionMenuItemView) findViewById(R.id.action_favorite);
            fav.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transits, menu);
        MenuItem fav = (MenuItem) findViewById(R.id.action_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_cars:
            {
                currentKind = CARS;
                fillTableLayout(checkpoints);
                break;
            }
            case R.id.action_trucks:
            {
                currentKind = TRUCKS;
                fillTableLayout(checkpoints);
                break;
            }
            case R.id.action_busses:
            {
                currentKind = BUSSES;
                fillTableLayout(checkpoints);
                break;
            }
            case R.id.action_favorite:
            {
                if (helper.isFavoriteBorder(borderId))
                {
                    helper.delFavoriteBorder(borderId);
                    ActionMenuItemView fav = (ActionMenuItemView) findViewById(R.id.action_favorite);
                    fav.setIcon(getResources().getDrawable(R.drawable.ic_favorite_outline_black_24dp));
                } else
                {
                    helper.addFavoriteBorder(borderId);
                    ActionMenuItemView fav = (ActionMenuItemView) findViewById(R.id.action_favorite);
                    fav.setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setInfoAboutBorder(String info)
    {
        TextView textViewInfo = (TextView) findViewById(R.id.textViewInfoBorder);
        textViewInfo.setText(info);
    }



}
