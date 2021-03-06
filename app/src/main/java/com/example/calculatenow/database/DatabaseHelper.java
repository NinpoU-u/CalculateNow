package com.example.calculatenow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datalist.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EQUATION_TABLE = "CREATE TABLE " +
                DataContract.DataEntry.TABLE_NAME + " (" +
                DataContract.DataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataContract.DataEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataContract.DataEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                DataContract.DataEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_EQUATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  DataContract.DataEntry.TABLE_NAME);
        onCreate(db);
    }



    /*public void deleteCallDetail(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataContract.DataEntry.TABLE_NAME, DataContract.DataEntry._ID + "=" + position, null);
        db.close();
    }*/
}

