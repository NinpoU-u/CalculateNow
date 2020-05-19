package com.example.calculatenow.adapter;

/**
 * Created by ravi on 20/02/18.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatenow.R;
import com.example.calculatenow.model.EquationData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EquationAdapter extends RecyclerView.Adapter<EquationAdapter.MyViewHolder> {

    private Context context;
    private List<EquationData> notesList;
    private int safePosition;
    //ItemClickListener itemClickListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView equation;
        TextView result;
        TextView timestamp;
        public ImageView setting;
        //public TextView dot;


        MyViewHolder(View view) {
            super(view);
            equation = view.findViewById(R.id.textview_operation_item);
            result = view.findViewById(R.id.textview_result);
            //dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.date_id);
        }
    }

    private Context getContext(){
        return context;
    }


    public EquationAdapter(Context context, List<EquationData> notesList) {
        this.context = context;
        this.notesList = notesList;
        //this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.database_history_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final EquationData note = notesList.get(position);


        holder.equation.setText(note.getEquation());
        holder.result.setText(note.getResult());

        /*// Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));*/

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(note.getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return notesList.size() ;
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    public void removeNote(int position) {
        notesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemChanged(position);
    }

    public void restoreNote(EquationData note, int position) {
        notesList.add(position,note);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void sendEquation(int position){
        final String equation = notesList.get(position).getEquation();
        final String result = notesList.get(position).getResult();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Your calculation is "
                + equation +
                " = " + result);
        intent.setType("text/plain");
        getContext().startActivity(Intent.createChooser(intent, "Send To"));
    }
}
