package com.example.calculatenow.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.database_history_item, viewGroup, false);
        return new SwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeViewHolder swipeViewHolder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        viewBinderHelper.setOpenOnlyOne(true);
        final String name = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME));
        final String amount = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_AMOUNT));
        long id = mCursor.getLong(mCursor.getColumnIndex(DataContract.DataEntry._ID));
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        viewBinderHelper.bind(swipeViewHolder.swipelayout, name); //employees.get(position).getName())
        viewBinderHelper.bind(swipeViewHolder.swipelayout, amount);
        viewBinderHelper.bind(swipeViewHolder.swipelayout, currentDate);
        viewBinderHelper.closeLayout(String.valueOf(id));


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
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class SwipeViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private TextView countText;
        private TextView dateText;
        private TextView share_but;
        private TextView txtDelete;
        private SwipeRevealLayout swipelayout;

        SwipeViewHolder(@NonNull View itemView) {
            super(itemView);

            dateText = itemView.findViewById(R.id.date_id);
            countText = itemView.findViewById(R.id.textview_amount_item);
            nameText = itemView.findViewById(R.id.textview_result);
            share_but = itemView.findViewById(R.id.share_but);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            swipelayout = itemView.findViewById(R.id.swipelayout);

            share_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "share", Toast.LENGTH_SHORT).show();
                }
            });

            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
