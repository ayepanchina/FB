package com.example.app;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Вадим on 18.05.14.
 */
public class ExpenseSummaryAdapter extends SimpleCursorAdapter {

    Context context;
    private SummaryFragment fragment;

    public ExpenseSummaryAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.setFragment(fragment);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        String catName = c.getString(c.getColumnIndex(DBHelper.CATEGORY_NAME));
        final String id = c.getString(c.getColumnIndex(DBHelper.TABLE_ID));
        //String date = c.getString(c.getColumnIndex(DBHelper.ITEM_DATE));
        long fact = c.getInt(c.getColumnIndex("sum(" + DBHelper.ITEM_VALUE + ")"));
        long plan = c.getInt(c.getColumnIndex(DBHelper.CATEGORY_PLAN_VALUE));
        TextView textCatName = (TextView) view.findViewById(R.id.textCatName);
        TextView textFactPlan = (TextView) view.findViewById(R.id.textFactPlanValue);
        ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar);

        // Set Drawable (Color)
        if (getStatusInPercents(plan, fact) > 95) {
            bar.setProgressDrawable(context.getResources().getDrawable(R.drawable.appthemeredbudget_progress_horizontal_holo_light));
        }
        else if (getStatusInPercents(plan, fact) > 70) {
            bar.setProgressDrawable(context.getResources().getDrawable(R.drawable.appthemeyellowbudget_progress_horizontal_holo_light));
        }

        bar.setMax((int) plan);
        bar.setProgress((int) fact);
        bar.setSecondaryProgress(50);
        textCatName.setText(catName);
        textFactPlan.setText("(" + fact + "/" + plan + ")");

        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment().showTransactionDialog(id);
            }
        });

    }

    public int getStatusInPercents(float plan, float fact) {
        return (int) (fact * 100 / plan);
    }

    public SummaryFragment getFragment() {
        return fragment;
    }

    public void setFragment(SummaryFragment fragment) {
        this.fragment = fragment;
    }
}
