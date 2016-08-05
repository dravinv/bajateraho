package com.awesome.dravin.appdemo1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dravin on 6/4/2016.
 */
public class DVdatabase_adapter {

    DVhelper helper;

    public DVdatabase_adapter(Context context) {
        helper = new DVhelper(context);
    }


    public long insertData(String name, String password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DVhelper.NAME, name);
        contentValues.put(DVhelper.PASSWORD, password);
        long id = db.insert(DVhelper.TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }

    public String getAllUri() {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DVhelper.UID, DVhelper.NAME, DVhelper.PASSWORD};
        Cursor cursor = db.query(DVhelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            //int index1 = cursor.getColumnIndex(DVhelper.UID);
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            stringBuffer.append(password + "\n");
        }
        return stringBuffer.toString();

    }

    public String getAllName() {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DVhelper.UID, DVhelper.NAME, DVhelper.PASSWORD};
        Cursor cursor = db.query(DVhelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            //int index1 = cursor.getColumnIndex(DVhelper.UID);
            int cid = cursor.getInt(0);
            String name = cursor.getString(1);
            String password = cursor.getString(2);
            stringBuffer.append(name + "\n");
        }
        return stringBuffer.toString();

    }

    public String getData(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DVhelper.NAME, DVhelper.PASSWORD};
        Cursor cursor = db.query(DVhelper.TABLE_NAME, columns, DVhelper.NAME + "='" + name + "'", null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()) {
            //int index1 = cursor.getColumnIndex(DVhelper.UID);
            int index1 = cursor.getColumnIndex(DVhelper.NAME);
            int index2 = cursor.getColumnIndex(DVhelper.PASSWORD);
            String personName = cursor.getString(index1);
            String password = cursor.getString(index2);
            stringBuffer.append(personName + " " + password + "\n");
        }
        return stringBuffer.toString();
    }

    public void deleteDB() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DVhelper.TABLE_NAME, null, null);
    }


    static class DVhelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "dvdatabase";
        private static final String TABLE_NAME = "DVTABLE";
        private static final int DB_VERSION = 1;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String PASSWORD = "Password";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + " " + "(" + UID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
                + " VARCHAR(255)," + PASSWORD + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE  IF EXISTS" + TABLE_NAME;
        private Context context;

        public DVhelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);

        }
    }
}
