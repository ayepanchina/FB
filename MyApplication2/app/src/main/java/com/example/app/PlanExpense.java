package com.example.app;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlanExpense extends Activity {

    static DatabaseProvider prov;
    static Float sumExpense;
    static Float income;
    DBHelper dbOpen;
    SQLiteDatabase base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_expense);
        dbOpen = new DBHelper(this);
        base = dbOpen.getWritableDatabase();
        this.prov=new DatabaseProvider(dbOpen);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plan_expense, menu);
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
    public static class PlaceholderFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

        Cursor cursor;
        ExpensePlanAdapter adapter;
        AbsListView.OnScrollListener onScrollListener;
        private float sum = 0;
        List<ExpensePlanValue> expenseValues;

        private static final String[] FROM = {DBHelper.CATEGORY_NAME,DBHelper.CATEGORY_PLAN_VALUE, "sum(" + DBHelper.ITEM_VALUE + ")"};
        private static final int[] TO = {R.id.textCategory,R.id.editTextValue};
        TextView planSum;

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            cursor = prov.getExpenseCategories();
            this.adapter = new ExpensePlanAdapter(getActivity(), R.layout.plan_expense_item, getCursor(), FROM, TO, 0);
            getLoaderManager().initLoader(0, savedInstanceState, this);
            setListAdapter(adapter);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // onScroll
            onScrollListener = new AbsListView.OnScrollListener() {
                int oldFirstVisibleItem = 0;
                int oldLastVisibleItem = 0;

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem > oldFirstVisibleItem) {
                        for(int i = oldFirstVisibleItem; i < firstVisibleItem; i++) {
                            onItemLeavesVisibleZone(i);
                        }
                    }
                    if (firstVisibleItem < oldFirstVisibleItem) {
                        for(int i = firstVisibleItem; i < oldFirstVisibleItem; i++) {
                            onItemEntersVisibleZone(i);
                        }
                    }

                    int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
                    if (lastVisibleItem < oldLastVisibleItem) {
                        for(int i = oldLastVisibleItem+1; i <= lastVisibleItem; i++) {
                            onItemLeavesVisibleZone(i);
                        }
                    }
                    if (lastVisibleItem > oldLastVisibleItem) {
                        for(int i = oldLastVisibleItem+1; i <= lastVisibleItem; i++) {
                            onItemEntersVisibleZone(i);
                        }
                    }

                    oldFirstVisibleItem = firstVisibleItem;
                    oldLastVisibleItem = lastVisibleItem;
                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }
            };
        }

        public void onItemEntersVisibleZone(int position) {
            ExpensePlanValue value = expenseValues.get(position);

            // Restore previously entered value from collection if it different from default data
            if (value.isEdited()) {
                EditText editText = (EditText) getListView().getChildAt(position).findViewById(R.id.editTextValue);
                editText.setText(String.valueOf(value.getValue()));
            }
        }

        public void onItemLeavesVisibleZone(int position) {
            // Compare with value in collection, set new value only if different from previous
            EditText editText = (EditText) getListView().getChildAt(position).findViewById(R.id.editTextValue);
            String valueString = editText.getText().toString();
            ExpensePlanValue planValue = expenseValues.get(position);

            if (!String.valueOf(planValue.getValue()).equals(valueString) || !String.valueOf(planValue.getPrevValue()).equals(valueString)) {
                // Format value
                if (valueString.startsWith(".")) {
                    valueString = valueString.replaceFirst(".", "0.");
                    editText.setText(valueString);
                    editText.setSelection(valueString.length());
                }
                if (valueString.endsWith(".")) {
                    valueString += "0";
                }

                float value = Float.parseFloat(valueString);
                planValue.setValue(value);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_plan_expense, container, false);

            //TODO: Find way to remove this spike!
            planSum = (TextView) rootView.findViewById(R.id.textViewPlanSum);
            planSum.setText(Float.toString(sum));

            Button buttonSet = (Button) rootView.findViewById(R.id.button);
            buttonSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expenseValues = getAdapter().getExpensePlanValueList();

                    for (ExpensePlanValue value : expenseValues) {
                        if (!value.isEdited())
                            continue;
                        prov.setPlanValueToCategoryId(String.valueOf(value.getId()), value.getValue());
                    }
                    getLoaderManager().getLoader(0).forceLoad();
                }
            });

            return rootView;
        }

        public ExpensePlanAdapter getAdapter() {
            return adapter;
        }

        public Cursor getCursor() {
            return cursor;
        }

        public float calculateExpenseSumValue() {
            float sum = 0;

            for (ExpensePlanValue value : expenseValues) {
                sum += value.getValue();
            }

            return sum;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new MainLoader(getActivity(), prov, 0);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            getAdapter().swapCursor(cursor);
            Log.d("FUCK", cursor.toString());
            this.expenseValues = null;
            //setListenersToEditViews();
            //sum = calculateExpenseSumValue();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
