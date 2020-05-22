package com.example.calculatenow.adapter;

/*
  Created by NinpoU-u on 20/05/20.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    private Context getContext(){
        return context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView equation;
        TextView result;
        TextView timeStampDate;
        TextView timeStampTime;
        TextView equal;
        //public ImageView setting;


        MyViewHolder(View view) {
            super(view);
            equation = view.findViewById(R.id.textView_operation_item);
            result = view.findViewById(R.id.textView_result);
            timeStampDate = view.findViewById(R.id.date_id);
            timeStampTime = view.findViewById(R.id.time_id);
            equal = view.findViewById(R.id.equally);
        }
    }


    public EquationAdapter(Context context, List<EquationData> notesList) {
        this.context = context;
        this.notesList = notesList;
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
        final EquationData equation= notesList.get(position);

        holder.equation.setText(equation.getEquation());
        holder.result.setText(equation.getResult());
        holder.timeStampDate.setText(formatDate(equation.getTimestamp()));
        holder.timeStampTime.setText(formatTime(equation.getTimestamp()));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean("pref_dark", false)){
            holder.equation.setTextColor(Color.WHITE);
            holder.equal.setTextColor(Color.WHITE);
            holder.result.setTextColor(Color.WHITE);
        }else{
            holder.equation.setTextColor(Color.BLACK);
            holder.equal.setTextColor(Color.BLACK);
            holder.result.setTextColor(Color.BLACK);
        }
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
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd");
            return fmtOut.format(date);
        } catch (ParseException ignored) {

        }
        return "";
    }

    private String formatTime(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
            return fmtOut.format(date);
        } catch (ParseException ignored) {

        }
        return "";
    }

    /*public void removeEq(int position) {
        eqList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemChanged(position);
    }*/

    /*public void restoreEq(EquationData note, int position) {
        eqList.add(position,equation);
        // notify item added by position
        notifyItemInserted(position);
    }*/

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
