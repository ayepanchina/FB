package com.example.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;

public class PlanFailed extends Activity {

    static DatabaseProvider prov;
    static long newIncome;
    static  long newOutcome;
    static  long needed;
    DBHelper dbOpen;
    SQLiteDatabase base;
   static float reserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_failed);

        dbOpen = new DBHelper(this);
        base = dbOpen.getWritableDatabase();
        this.prov=new DatabaseProvider(dbOpen);

        Intent intent = getIntent();
        reserve = intent.getFloatExtra("reserve",-1);
        newIncome = Math.round(intent.getFloatExtra("newIncomes",-1));
        newOutcome = Math.round(intent.getFloatExtra("newOutcomes",-1));
        needed = Math.round(intent.getFloatExtra("needed",-1));
               if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plan_failed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_plan_failed, container, false);
            TextView first = (TextView)rootView.findViewById(R.id.textFood);
            TextView second = (TextView)rootView.findViewById(R.id.textViewIncome);
            TextView neededText = (TextView)rootView.findViewById(R.id.textViewBill);
            first.setText(getString(R.string.incomes_should_be) + newIncome + " per Month");
            second.setText(getString(R.string.outcomes_should_be) + newOutcome + " per Month");
            neededText.setText(getString(R.string.needed) + needed + " €");
            ArrayList<Bar> points = new ArrayList<Bar>();
            Bar d = new Bar();
            d.setColor(Color.parseColor("#99CC00"));
            d.setName(getString(R.string.reserve));
            d.setValue(reserve);
            Bar d2 = new Bar();
            d2.setColor(Color.parseColor("#FFBB33"));
            d2.setName(getString(R.string.plan_expense));
            d2.setValue(prov.getPlanValue(null,"bigExpense"));
            points.add(d);
            points.add(d2);

            BarGraph g = (BarGraph)rootView.findViewById(R.id.bargraph3);
            assert g != null;
            g.setUnit("€");
            g.appendUnit(true);
            g.setBars(points);

            g.setOnBarClickedListener(new BarGraph.OnBarClickedListener(){

                @Override
                public void onClick(int index) {

                }

            });
            return rootView;
        }
    }

}
