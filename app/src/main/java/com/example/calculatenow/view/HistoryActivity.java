package com.example.calculatenow.view;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatenow.R;
import com.example.calculatenow.adapter.DataAdapter;
import com.example.calculatenow.database.DataContract;
import com.example.calculatenow.database.DatabaseHelper;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HistoryActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private DataAdapter mAdapter;
    private TextView emptyView;
    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sp.getBoolean("pref_dark", false))
            switch (sp.getString("pref_theme", "0")) {
                case "0":
                    setTheme(R.style.AppTheme_Dark_Blue);
                    break;
                case "1":
                    setTheme(R.style.AppTheme_Dark_Cyan);
                    break;
                case "2":
                    setTheme(R.style.AppTheme_Dark_Gray);
                    break;
                case "3":
                    setTheme(R.style.AppTheme_Dark_Green);
                    break;
                case "4":
                    setTheme(R.style.AppTheme_Dark_Purple);
                    break;
                case "5":
                    setTheme(R.style.AppTheme_Dark_Red);
                    break;
            }
        else
            switch (sp.getString("pref_theme", "0")) {
                case "0":
                    setTheme(R.style.AppTheme_Light_Blue);
                    break;
                case "1":
                    setTheme(R.style.AppTheme_Light_Cyan);
                    break;
                case "2":
                    setTheme(R.style.AppTheme_Light_Gray);
                    break;
                case "3":
                    setTheme(R.style.AppTheme_Light_Green);
                    break;
                case "4":
                    setTheme(R.style.AppTheme_Light_Purple);
                    break;
                case "5":
                    setTheme(R.style.AppTheme_Light_Red);
                    break;
            }


        setContentView(R.layout.activity_history);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_history));
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new DataAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);


        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

       new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                switch (direction){
                    case ItemTouchHelper.LEFT:
                        mAdapter.sendEquation();
                        mAdapter.swapCursor(getAllItems());
                        break;
                    case ItemTouchHelper.RIGHT:
                        removeItem((int) viewHolder.itemView.getTag());
                        /*Snackbar.make(recyclerView, (Integer) viewHolder.itemView.getTag(),Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();*/
                        break;
                }
            }

           @Override
           public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

               new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                       .addSwipeLeftBackgroundColor(ContextCompat.getColor(HistoryActivity.this, R.color.green))
                       .addSwipeLeftActionIcon(R.drawable.ic_share_black_24dp)
                       .addSwipeRightBackgroundColor(ContextCompat.getColor(HistoryActivity.this, R.color.red))
                       .addSwipeRightActionIcon(R.drawable.ic_delete_forever_black_24dp)
                       .create()
                       .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
           }
       }).attachToRecyclerView(recyclerView);

    }

    public void removeItem(int position) {
        mDatabase.delete(DataContract.DataEntry.TABLE_NAME, DataContract.DataEntry._ID + "=" + position, null);
        mAdapter.swapCursor(getAllItems());
        mAdapter.notifyItemRemoved(position);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



