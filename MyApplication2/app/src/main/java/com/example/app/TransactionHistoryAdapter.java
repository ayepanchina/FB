package com.example.app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Анастасия on 14.04.14.
 */
public class TransactionHistoryAdapter extends SimpleCursorAdapter{

    public TransactionHistoryAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        String value = c.getString(c.getColumnIndex(DBHelper.ITEM_VALUE));
        String date = c.getString(c.getColumnIndex(DBHelper.ITEM_DATE));
        int type = c.getInt(c.getColumnIndex(DBHelper.CATEGORY_TYPE));


        ImageView typeImage=(ImageView)view.findViewById(R.id.imageViewType);
        if (type == 0) {
            typeImage.setImageResource(R.drawable.income);
        } else {typeImage.setImageResource(R.drawable.outcome);}

        TextView valueText = (TextView) view.findViewById(R.id.textName);
        if (valueText != null) {
            valueText.setText(value);
        }

        TextView dateText = (TextView) view.findViewById(R.id.itemDate);
        if (dateText != null) {
            Date date1=null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
               date1 =dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("myLog",e.getMessage());
                //Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG);
            }
            dateText.setText(String.format("%1$tb %1$td,  %1$tY", date1));

        }


    }
}
