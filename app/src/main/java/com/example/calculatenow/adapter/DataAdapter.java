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

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private int selectedPosition = -1;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public DataAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView countText;
        private TextView txtShare;
        private TextView dateText;
        private TextView share_but;
        private TextView txtDelete;
        private SwipeRevealLayout swipelayout;

        public DataViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.textview_result);
            countText = itemView.findViewById(R.id.textview_amount_item);
            txtShare = itemView.findViewById(R.id.share_but);
            dateText = itemView.findViewById(R.id.date_id);
            share_but = itemView.findViewById(R.id.share_but);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            swipelayout = itemView.findViewById(R.id.swipelayout);

        }
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String name = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME));
        final String amount = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_AMOUNT));
        long id = mCursor.getLong(mCursor.getColumnIndex(DataContract.DataEntry._ID));
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        holder.nameText.setText(name);
        holder.countText.setText(amount);
        holder.itemView.setTag(id);
        holder.dateText.setText(currentDate);
        holder.txtShare.setOnClickListener(new View.OnClickListener() {
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


    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
