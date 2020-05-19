package com.example.calculatenow.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.calculatenow.model.EquationData;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static   DatabaseHelper sInstance;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";

    //SINGLETON
    public static DatabaseHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    //Private constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(EquationData.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + EquationData.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertEquation(String equation, String result) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(EquationData.COLUMN_EQUATION, equation);
        values.put(EquationData.COLUMN_RESULT, result);

        // insert row
        long id = db.insert(EquationData.TABLE_NAME, null, values);
        Log.e(TAG, "insertNote:id "+String.valueOf(id) );

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public EquationData getEquation(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(EquationData.TABLE_NAME,
                new String[]{EquationData.COLUMN_ID, EquationData.COLUMN_EQUATION, EquationData.COLUMN_RESULT, EquationData.COLUMN_TIMESTAMP},
                EquationData.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        EquationData note = new EquationData(
                cursor.getInt(cursor.getColumnIndex(EquationData.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(EquationData.COLUMN_EQUATION)),
                cursor.getString(cursor.getColumnIndex(EquationData.COLUMN_RESULT)),
                cursor.getString(cursor.getColumnIndex(EquationData.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<EquationData> getAllEq() {
        List<EquationData> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + EquationData.TABLE_NAME + " ORDER BY " +
                EquationData.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EquationData note = new EquationData();
                note.setId(cursor.getInt(cursor.getColumnIndex(EquationData.COLUMN_ID)));
                note.setEquation(cursor.getString(cursor.getColumnIndex(EquationData.COLUMN_EQUATION)));
                note.setResult(cursor.getString(cursor.getColumnIndex(EquationData.COLUMN_RESULT)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(EquationData.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + EquationData.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(EquationData note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EquationData.COLUMN_EQUATION, note.getEquation());
        values.put(EquationData.COLUMN_RESULT, note.getResult());

        // updating row
        return db.update(EquationData.TABLE_NAME, values, EquationData.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(EquationData note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EquationData.TABLE_NAME, EquationData.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
