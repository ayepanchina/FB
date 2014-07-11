package com.example.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.echo.holographlibrary.Square;

import java.util.Random;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionReport#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TransactionReport extends android.app.Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static DatabaseProvider prov;
    DBHelper dbOpen;
    Cursor cursor;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param dbOpen@return A new instance of fragment TransactionReport.
     */
    // TODO: Rename and change types and number of parameters

    public TransactionReport(DBHelper dbOpen) {
        this.dbOpen = dbOpen;
        this.prov = new DatabaseProvider(dbOpen);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_transaction_report, container, false);

        PieGraph pg = (PieGraph)v.findViewById(R.id.graph);
        Square sq=(Square)v.findViewById(R.id.ololo52);
        PieSlice slice;
        cursor=prov.getSumOfFourExpenseValues();
        Random rnd = new Random();
        int color;
        long sumOfPopularCategories=0;
        long allSumOfExpenses;
        if (cursor != null && cursor.moveToFirst()){
            do
            {
                sumOfPopularCategories += cursor.getLong(cursor.getColumnIndex("sum(value)"));
                color = Color.argb(250, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                slice = new PieSlice();
                slice.setColor(color);
                slice.setValue(cursor.getLong(cursor.getColumnIndex("sum(value)")));
                slice.setTitle(cursor.getString(cursor.getColumnIndex("cat_name")) + " = " + cursor.getLong(cursor.getColumnIndex("sum(value)"))+" ₴");
                pg.addSlice(slice);
                sq.addSlice(slice);
            }
            while(cursor.moveToNext());
            cursor.close();
        }
    cursor = prov.getSumExpenseValues();
        if (cursor != null && cursor.moveToFirst()){
            allSumOfExpenses = cursor.getLong(cursor.getColumnIndex("sum(value)"));
            color = Color.argb(250, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            slice = new PieSlice();
            slice.setColor(color);
            slice.setValue(allSumOfExpenses-sumOfPopularCategories);
            slice.setTitle("others" + " = " + (allSumOfExpenses-sumOfPopularCategories) +" ₴");
            pg.addSlice(slice);
            sq.addSlice(slice);
        }

        return v;
    }
    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
    }


