package com.lilsmile.borderonlineproductionedition.app;

import android.content.ComponentCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Smile on 28.03.16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "BorderOnlineDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table continents ("
                        + "id integer primary key, "
                        + "name text, "
                        + "enable integer);"
        );
        sqLiteDatabase.execSQL("create table borders ("
                        + "id integer primary key, "
                        + "countryOne text, "
                        + "countryTwo text, "
                        + "continentID integer, "
                        + "additionalInfo text, "
                        + "isFav integer);"

        );
        sqLiteDatabase.execSQL("create table checkpoints ("
                        + "id integer primary key, "
                        + "borderID integer, "
                        + "forwardLocality text, "
                        + "backwardLocality text, "
                        + "carByUsers integer, "
                        + "busByUsers integer, "
                        + "truckByUsers integer, "
                        + "carByOfficial integer, "
                        + "busByOfficial integer, "
                        + "truckByOfficial integer, "
                        + "isFav integer);"
        );
        sqLiteDatabase.execSQL("create table favorite ("
                        + "id integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addContinents(Classes.Continent[] array)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("continents", null, null);
        for (int i = 0; i<array.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put("id", array[i].id );
            cv.put("name", array[i].name);
            cv.put("enable", array[i].enable);
            db.insert("continents",null, cv);
        }
    }

    public void addBorders(Classes.Border[] array, int continentID)
    {
        SQLiteDatabase db = getWritableDatabase();
        for (Classes.Border border : array)
        {
            ContentValues cv = new ContentValues();
            cv.put("id", border.id);
            cv.put("countryOne", border.country_1);
            cv.put("countryTwo", border.country_2);
            cv.put("additionalInfo", border.additional_info);
            cv.put("continentID", continentID);
            db.insert("borders", null, cv);
        }
    }

    public void changeBorders(Classes.Border[] array, int continentID)
    {
        SQLiteDatabase db = getWritableDatabase();
        for (Classes.Border border : array)
        {
            ContentValues cv = new ContentValues();
            cv.put("id", border.id);
            cv.put("countryOne", border.country_1);
            cv.put("countryTwo", border.country_2);
            cv.put("additionalInfo", border.additional_info);
            cv.put("continentID", continentID);
            String[] args = new String[]{String.valueOf(border.id)};
            db.update("borders",cv, "id = ?", args);
        }
    }

    public Classes.Border[] getBorders(int continentID)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("borders", null, "continentID = ?", new String[]{String.valueOf(continentID)}, null, null, null);
        if (c.moveToFirst())
        {
            int idColIndex = c.getColumnIndex("id");
            int countryOneColIndex = c.getColumnIndex("countryOne");
            int countryTwoColIndex = c.getColumnIndex("countryTwo");
            int additionalInfoColIndex = c.getColumnIndex("additionalInfo");
            Classes.Border[] array = new Classes.Border[c.getCount()];
            int i = 0;
            do
            {
                Classes.Border border = new Classes().new Border();
                border.country_1 = c.getString(countryOneColIndex);
                border.country_2 = c.getString(countryTwoColIndex);
                border.id = c.getInt(idColIndex);
                border.additional_info = c.getString(additionalInfoColIndex);
                array[i] = border;
                i++;
            } while (c.moveToNext());
            return array;
        }
        return null;
    }


    public Classes.Checkpoint[] getCheckpoints(int borderID)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("checkpoints", null, "borderID = ?", new String[]{String.valueOf(borderID)}, null, null, null);
        if (c.moveToFirst())
        {
            int idColIndex = c.getColumnIndex("id");
            int flColIndex = c.getColumnIndex("forwardLocality");
            int blColIndex = c.getColumnIndex("backwardLOcality");
            int cBUColIndex = c.getColumnIndex("carByUsers");
            int bBUColIndex = c.getColumnIndex("busByUsers");
            int tBUColIndex = c.getColumnIndex("truckByUsers");
            int cBOColIndex = c.getColumnIndex("carByOfficial");
            int bBOColIndex = c.getColumnIndex("busByOfficial");
            int tBOColIndex = c.getColumnIndex("truckByOfficial");
            Classes.Checkpoint[] array = new Classes.Checkpoint[c.getCount()];
            int i = 0;
            do
            {
                Classes.Checkpoint checkpoint = new Classes().new Checkpoint();
                checkpoint.id = c.getInt(idColIndex);
                checkpoint.forward_locality = c.getString(flColIndex);
                checkpoint.backward_locality = c.getString(blColIndex);
                checkpoint.queue.by_users.car = c.getInt(cBUColIndex);
                checkpoint.queue.by_users.bus = c.getInt(bBUColIndex);
                checkpoint.queue.by_users.truck = c.getInt(tBUColIndex);
                checkpoint.queue.official.car = c.getInt(cBOColIndex);
                checkpoint.queue.official.bus = c.getInt(cBOColIndex);
                checkpoint.queue.official.truck = c.getInt(tBOColIndex);
                array[i] = checkpoint;
                i++;
            } while (c.moveToNext());
            return array;
        }
        return null;
    }

    public void addCheckpoints(Classes.Checkpoint[] array, int borderID)
    {
        SQLiteDatabase db = getWritableDatabase();
        for (Classes.Checkpoint checkpoint : array)
        {
            ContentValues cv = new ContentValues();
            cv.put("borderID", borderID);
            cv.put("id", checkpoint.id);
            cv.put("forwardLocality", checkpoint.forward_locality);
            cv.put("backwardLocality", checkpoint.backward_locality);
            cv.put("carByUsers", checkpoint.queue.by_users.car);
            cv.put("busByUsers", checkpoint.queue.by_users.bus);
            cv.put("truckByUsers", checkpoint.queue.by_users.truck);
            cv.put("carByOfficial", checkpoint.queue.official.car);
            cv.put("busByOfficial", checkpoint.queue.official.bus);
            cv.put("truckByOfficial", checkpoint.queue.official.truck);

            db.insert("checkpoints", null, cv);

        }
    }

    public void changeCheckpoints(Classes.Checkpoint[] array, int borderId)
    {
        SQLiteDatabase db = getWritableDatabase();
        for (Classes.Checkpoint checkpoint : array)
        {
            ContentValues cv = new ContentValues();
            cv.put("borderID", borderId);
            cv.put("id", checkpoint.id);
            cv.put("forwardLocality", checkpoint.forward_locality);
            cv.put("backwardLocality", checkpoint.backward_locality);
            cv.put("carByUsers", checkpoint.queue.by_users.car);
            cv.put("busByUsers", checkpoint.queue.by_users.bus);
            cv.put("truckByUsers", checkpoint.queue.by_users.truck);
            cv.put("carByOfficial", checkpoint.queue.official.car);
            cv.put("busByOfficial", checkpoint.queue.official.bus);
            cv.put("truckByOfficial", checkpoint.queue.official.truck);
            db.update("checkpoints", cv, "id = ", new String[]{String.valueOf(checkpoint.id)});

        }
    }

    public void addFavoriteBorder(int borderId)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", borderId);
        db.insert("favorite", null, cv);
    }

    public void delFavoriteBorder(int borderId)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("favorite", "id = ?", new String[]{String.valueOf(borderId)});
    }

    public boolean isFavoriteBorder(int borderId)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("favorite", null, "id = ?", new String[]{String.valueOf(borderId)}, null, null, null);
        if (c.moveToFirst())
        {
            return true;
        } else
        {
            return false;
        }
    }





}
