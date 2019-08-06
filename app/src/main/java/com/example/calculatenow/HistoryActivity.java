package com.example.calculatenow;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatenow.adapter.DataAdapter;
import com.example.calculatenow.database.DataContract;
import com.example.calculatenow.database.DatabaseHelper;
import com.r0adkll.slidr.Slidr;

public class HistoryActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private DataAdapter mAdapter;
    private TextView emptyView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Slidr.attach(this);


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DataAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);


            if (mAdapter == null || mAdapter.getItemCount() == 0) {

                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);


    }


    /*private void addItem() {

        if (mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) {
            return;
        }

        String name = mEditTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(DataContract.DataEntry.COLUMN_NAME, name);
        cv.put(DataContract.DataEntry.COLUMN_AMOUNT, mAmount);

        mDatabase.insert(DataContract.DataEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());

        mEditTextName.getText().clear();
    }*/

    private void removeItem(long id){
        mDatabase.delete(DataContract.DataEntry.TABLE_NAME,
                DataContract.DataEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                DataContract.DataEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataContract.DataEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }


}



