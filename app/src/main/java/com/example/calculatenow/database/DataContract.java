package com.example.calculatenow.database;

import android.provider.BaseColumns;

public class DataContract {

    private DataContract() {
    }

    public static final class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "dataList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}