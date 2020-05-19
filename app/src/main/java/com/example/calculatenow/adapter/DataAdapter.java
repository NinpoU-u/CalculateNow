package com.example.calculatenow.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatenow.R;

import java.util.Calendar;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    //private int selectedPosition = -1;
    private Cursor mCursor;
    private Context mContext;


    public DataAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView countText;
        private TextView dateText;

        DataViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.textview_result);
            countText = itemView.findViewById(R.id.textview_amount_item);
            dateText = itemView.findViewById(R.id.date_id);
        }
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.database_history_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        //Calendar
        Calendar calendar = Calendar.getInstance();


        //Database
        /*final String name = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME));
        final String amount = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_AMOUNT));
        int id = mCursor.getInt(mCursor.getColumnIndex(DataContract.DataEntry._ID));
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());*/

        /*holder.nameText.setText(name);
        holder.countText.setText(amount);
        holder.itemView.setTag(id);
        holder.dateText.setText(currentDate);*/

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /*public void sendEquation(){
        final String name = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME));
        final String amount = mCursor.getString(mCursor.getColumnIndex(DataContract.DataEntry.COLUMN_AMOUNT));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Your calculation is "
                + amount +
                " = " + name);
        intent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(intent, "Send To"));
    }
*/
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
