package com.example.calculatenow.view;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
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
import com.example.calculatenow.adapter.EquationAdapter;
import com.example.calculatenow.database.DatabaseHelper;
import com.example.calculatenow.model.EquationData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HistoryActivity extends AppCompatActivity {
    private TextView emptyView;

    private EquationData deletedItemEquation;
    private EquationData deletedItemResult;
    private int deletedIndex;
    private DatabaseHelper db;
    private List<EquationData> equationList = new ArrayList<>();
    private EquationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db =  DatabaseHelper.getInstance(this);

        if (equationList != null) {
            equationList.addAll(db.getAllEq());
        }

        //Shared prefs to send data between 2 activities
        SharedPreferences equationProcess = getSharedPreferences("my_prefs", 0);
        String equation = equationProcess.getString("operation", "");
        String result = equationProcess.getString("result", "");

        createEquation(equation, result);



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
        emptyView = findViewById(R.id.empty_view);


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new EquationAdapter(this, equationList);
        recyclerView.setAdapter(mAdapter);


        toggleEmptyNotes();

       new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (viewHolder instanceof EquationAdapter.MyViewHolder) {
                    int position = viewHolder.getAdapterPosition();

                    String note = equationList.get(viewHolder.getAdapterPosition()).getEquation();
                    // backup of removed item for undo purpose
                    deletedItemEquation = equationList.get(viewHolder.getAdapterPosition());

                    deletedIndex = viewHolder.getAdapterPosition();
                    // remove the item from recycler view
                    deleteEquation(position);
                    //mAdapter.removeNote(viewHolder.getAdapterPosition());

                    Snackbar snackbar = Snackbar
                            .make(recyclerView, note + " removed from cart!", Snackbar.LENGTH_LONG);


                    snackbar.setAction("UNDO", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
                            // mAdapter.restoreNote(deletedItem, deletedIndex);
                            // Note note1 = new Note();
                            // note1.setId(deletedIndex);
                            // note1.getNote()
                            reCreateEquationSwipe(deletedItemEquation.getEquation(), deletedItemResult.getResult(), deletedIndex);

                        }
                    });
                    snackbar.setActionTextColor(Color.BLUE);
                    snackbar.show();
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

    //CREATE Equation in DataBase
    private void createEquation(String equation, String result) {

        long id = db.insertEquation(equation, result);

        // get the newly inserted note from db
        EquationData n = db.getEquation(id);

        if (n != null) {
            // adding new note to array list at 0 position
            equationList.add(0, n);

            // refreshing the list
            //mAdapter.notifyDataSetChanged();
        }
    }

    public void reCreateEquationSwipe(String equation, String result, int pos){
        long id = db.insertEquation(equation, result);

        // get the newly inserted note from db
        EquationData n = db.getEquation(id);

        if (n != null) {
            // adding new note to array list at 0 position
            equationList.add(pos, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateEquation(String note, int position) {
        EquationData n = equationList.get(position);
        // updating note text
        n.setEquation(note);

        // updating note in db
        db.updateNote(n);

        // refreshing the list
        equationList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteEquation(int position) {
        // deleting the note from db
        db.deleteNote(equationList.get(position));

        // removing the note from the list
        equationList.remove(position);

        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */


    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
            if (equationList.size() > 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
//        if (db.getNotesCount() > 0) {
//        } else {
//            noNotesView.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



