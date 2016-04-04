package com.lilsmile.borderonlineproductionedition.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Smile on 22.02.16.
 */
public class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
    Context context;

    DrawerItemClickListener(Context context)
    {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i)//position
        {
            case 0: //all borders
            {
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            }
            case 1: //favorites
            {
                break;
            }
            case 2: //recent
            {
                break;
            }
            case 3: //tall about queue
            {
                break;
            }
            case 4: //share
            {
                break;
            }
            case 5: //feedback
            {
                break;
            }
            case 6: //settings
            {
                context.startActivity(new Intent(context, SettingsActivity.class));
                break;
            }
        }
    }
}
