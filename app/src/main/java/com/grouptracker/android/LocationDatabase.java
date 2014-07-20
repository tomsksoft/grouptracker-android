package com.grouptracker.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tieru on 6/28/14.
 */
public class LocationDatabase {

    private static String DB_NAME = "location_tracking";
    private static String LOCATION_TABLE_NAME = "location";
    public static String LOCATION_TABLE_COLUMN1 = "trackid";
    public static String LOCATION_TABLE_COLUMN2 = "latitude";
    public static String LOCATION_TABLE_COLUMN3 = "longitude";

    public SQLiteDatabase db;
    private DbHelper dbHelper;
    Context context;

    public LocationDatabase(Context context){
        this.context = context;
        dbHelper=new DbHelper(context);
    }

    public class DbHelper extends SQLiteOpenHelper{
        public DbHelper(Context context){
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + LOCATION_TABLE_NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + LOCATION_TABLE_COLUMN1 +" integer, "
                    + LOCATION_TABLE_COLUMN2 +" text, "
                    + LOCATION_TABLE_COLUMN3 +" text "
                    + ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public long addLocation(int trackID, String latitude, String longitude){
            ContentValues value=new ContentValues();
            value.put(LOCATION_TABLE_COLUMN2, latitude);
            value.put(LOCATION_TABLE_COLUMN3, longitude);
        return db.insert(LOCATION_TABLE_NAME,null,value);
    }

    public int getLastTrackID(){
        int value;
        String query = "SELECT trackid FROM " + LOCATION_TABLE_NAME + " ORDER BY " + LOCATION_TABLE_COLUMN1 + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            value = cursor.getInt(cursor.getColumnIndex("trackid"));
        }
        else {
            value = -1;
        }
        cursor.close();
        return value;
    }

    public Cursor getLocations(int trackid){
        Log.d("MYAPP", "Got trackID: " +trackid);
        //String[] track = new String[] {String.valueOf(trackid)};
        String query = "SELECT * FROM " + LOCATION_TABLE_NAME + " WHERE trackid=" + trackid;
        Cursor cursor = db.rawQuery(query, null);
        /*Cursor cursor = db.query(LOCATION_TABLE_NAME, new String[]{
                LOCATION_TABLE_COLUMN1, LOCATION_TABLE_COLUMN2, LOCATION_TABLE_COLUMN3
        }, LOCATION_TABLE_COLUMN1 + "=",track, null, null, null);*/
        return cursor;
    }

    public Cursor getLocations(){
        String query = "SELECT * FROM " + LOCATION_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        /*Cursor cursor = db.query(LOCATION_TABLE_NAME, new String[]{
                LOCATION_TABLE_COLUMN1, LOCATION_TABLE_COLUMN2, LOCATION_TABLE_COLUMN3
        }, LOCATION_TABLE_COLUMN1 + "=",track, null, null, null);*/
        return cursor;
    }

    public int deleteAllLocations(){
        return db.delete(LOCATION_TABLE_NAME,null,null);
    }
}
