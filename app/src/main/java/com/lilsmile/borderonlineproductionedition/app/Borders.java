package com.lilsmile.borderonlineproductionedition.app;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.AdView;

import java.util.*;


public class Borders extends ActionBarActivity implements Constants, DirectDialog.OnDialogFinished {


    ListView listViewBorders;
    EditText searchEditText;
    Button buttonFind;
    AdView adView;

    SearchView searchView;

    Helper helper;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    SimpleAdapter simpleAdapter;

    String[] choosedCountries = new String[2];
    int choosedBorder=-1;

    String[] from = new String[]{COUNTRY_NAME_1, COUNTRY_NAME_2};
    int[] to = new int[]{R.id.textViewCountryName1, R.id.textViewCountryName2};

    Classes.Border[] borders = new Classes.Border[0];

    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borders);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_borders);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_borders);

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


        setTitle(getIntent().getStringExtra(EXTRA));
        helper = new Helper(this);

        listViewBorders = (ListView)findViewById(R.id.listViewBorders);
        //searchEditText = (EditText)findViewById(R.id.editTextSearch);
        //buttonFind = (Button)findViewById(R.id.buttonFind);
        adView = (AdView)findViewById(R.id.adViewBorders);

        adView.loadAd(helper.getAdRequest());

        //buttonFind.setOnClickListener(this);

        simpleAdapter = new SimpleAdapter(this, data, R.layout.one_border, from, to);
        listViewBorders.setAdapter(simpleAdapter);

        helper.getBorders(this, getIntent().getIntExtra(ID,1));

        listViewBorders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosedCountries[0] = borders[(int) l].country_1;
                choosedCountries[1] = borders[(int) l].country_2;
                choosedBorder = borders[(int)l].id;

                DirectDialog dialog = new DirectDialog(choosedCountries);
                dialog.show(getFragmentManager(), "direct");
            }
        });

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                showBorders(borders, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                showBorders(borders, s);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_borders, menu);
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
            startActivity(new Intent(Borders.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onLeftArrowClick() {
        String dataToSend = choosedCountries[1]+" -> "+choosedCountries[0];
        Intent intent = new Intent(Borders.this, Transits.class);
        intent.putExtra(EXTRA, dataToSend);
        intent.putExtra(ID,choosedBorder);
        intent.putExtra(DIRECTION, BACKWARD);
        startActivity(intent);
    }

    @Override
    public void onRightArrowClick() {
        String dataToSend = choosedCountries[0]+" -> "+choosedCountries[1];
        Intent intent = new Intent(Borders.this, Transits.class);
        intent.putExtra(EXTRA, dataToSend);
        intent.putExtra(ID,choosedBorder);
        intent.putExtra(DIRECTION, FORWARD);
        startActivity(intent);
    }


    public void showBorders(final Classes.Border borders[], String searchString)
    {
        this.borders = borders;
        data.clear();
        for (int i = 0; i< borders.length; i++)
        {
            if (searchString.equals("") || (borders[i].country_1.toLowerCase().contains(searchString) || borders[i].country_2.toLowerCase().contains(searchString)))
            {
                HashMap<String, Object> m = new HashMap<String, Object>();
                m.put(COUNTRY_NAME_1, borders[i].country_1);
                m.put(COUNTRY_NAME_2, borders[i].country_2);
                data.add(m);
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }

}
