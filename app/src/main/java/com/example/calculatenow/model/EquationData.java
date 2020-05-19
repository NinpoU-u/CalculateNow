package com.example.calculatenow.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NinpoU-u on 19/05/20.
 */

public class EquationData implements Parcelable {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EQUATION = "equation";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String mEquation;
    private String mResult;
    private String timestamp;
    //private Drawable image;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_EQUATION + " TEXT NOT NULL,"
                    + COLUMN_RESULT + " TEXT NOT NULL,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public EquationData() {
    }

    public EquationData(int id, String equation, String result, String timestamp) {
        this.id = id;
        this.mEquation = equation;
        this.mResult = result;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }

    public String getEquation() {
        return mEquation;
    }

    public void setEquation(String equation) {
        this.mEquation = equation;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String mResult) {
        this.mResult = mResult;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    protected EquationData(Parcel in) {
        id = in.readInt();
        mEquation = in.readString();
        mResult = in.readString();
        timestamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mEquation);
        dest.writeString(mResult);
        dest.writeString(timestamp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EquationData> CREATOR = new Parcelable.Creator<EquationData>() {
        @Override
        public EquationData createFromParcel(Parcel in) {
            return new EquationData(in);
        }

        @Override
        public EquationData[] newArray(int size) {
            return new EquationData[size];
        }
    };

    //Completed
}
