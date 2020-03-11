package com.example.calculatenow.adapter;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.calculatenow.R;
import com.example.calculatenow.database.DataContract;

import java.text.DateFormat;
import java.util.Calendar;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public SwipeAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.database_history_item, viewGroup, false);
        return new SwipeViewHolder(view); //+mListener
    }

    @Override
    public void onBindViewHolder(@NonNull final SwipeViewHolder swipeViewHolder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }


        final String name = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME));
        final String amount = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_AMOUNT));
        long id = mCursor.getLong(mCursor.getColumnIndex(DataContract.DataEntry._ID));
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        viewBinderHelper.setOpenOnlyOne(true);

        /*viewBinderHelper.bind(swipeViewHolder.swipelayout, name);
        viewBinderHelper.bind(swipeViewHolder.swipelayout, amount);
        viewBinderHelper.bind(swipeViewHolder.swipelayout, currentDate);
        viewBinderHelper.closeLayout(String.valueOf(id));*/


        swipeViewHolder.nameText.setText(name);
        swipeViewHolder.countText.setText(amount);
        swipeViewHolder.itemView.setTag(id);
        swipeViewHolder.dateText.setText(currentDate);
        swipeViewHolder.share_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Your calculation is "
                        + amount +
                        " = " + name);
                intent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(intent, "Send To"));
            }
        });

        /*swipeViewHolder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    static class SwipeViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView countText;
        private TextView dateText;
        private TextView share_but;
        private SwipeRevealLayout swipelayout;

        SwipeViewHolder(@NonNull View itemView) { //+final OnItemClickListener listener
            super(itemView);

            dateText = itemView.findViewById(R.id.date_id);
            countText = itemView.findViewById(R.id.textview_amount_item);
            nameText = itemView.findViewById(R.id.textview_result);
            share_but = itemView.findViewById(R.id.share_but);
            swipelayout = itemView.findViewById(R.id.swipelayout);


        }

    }


}
