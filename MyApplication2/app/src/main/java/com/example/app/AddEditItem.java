package com.example.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditItem extends Activity {
    static DatabaseProvider prov;
    DBHelper dbOpen;
    SQLiteDatabase base;
    static Long id;
    static Calendar cal;
    static boolean isEdit=false;
    Cursor cursor;
    private static float value;
    private static String itemDate;
    private static int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        dbOpen = new DBHelper(this);
        base = dbOpen.getWritableDatabase();
        this.prov=new DatabaseProvider(dbOpen);
        Intent intent = getIntent();
        id= intent.getLongExtra("some_id",-1);
        if (id!=-1) {
            isEdit=true;
            cursor=prov.getItemById(id);
            if (cursor.moveToFirst()){
                value=cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_VALUE));
                itemDate=cursor.getString(cursor.getColumnIndex(DBHelper.ITEM_DATE));
                catId=cursor.getInt(cursor.getColumnIndex(DBHelper.ITEM_CATEGORY_ID));
            }

            cursor.close();

    }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            isEdit = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add__edit__item, menu);
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
        String category="";
        public static TextView mDateTextView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_edit_item, container, false);
            final TextView valueText = (TextView) rootView.findViewById(R.id.editTextBill);
            mDateTextView = (TextView) rootView.findViewById(R.id.date);
            cal = Calendar.getInstance();
            Date date1=null;
            List<String> array_spinner=null;
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            // final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
            if (isEdit){

               valueText.setText(value+"");
                try {
                    date1 =dateFormat.parse(itemDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.i("myLog", e.getMessage());
                    //Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG);
                }
                array_spinner = prov.getAllCategoriesListForEditItem(catId);
               cal.setTime(date1);
                mDateTextView.setText(String.format("%1$tb %1$td,  %1$tY", cal.getTime()));

            } else {
                array_spinner = prov.getAllCategoriesList();
                valueText.setText(" ");
                mDateTextView.setText(String.format("%1$tb %1$td,  %1$tY",cal.getTime()));
            }


            final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerCategory);
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, array_spinner);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    category = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Button add=(Button)rootView.findViewById(R.id.addTransaction);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Time time = new Time();
                    time.setToNow();
                    String date = "";
                    if (isEdit) {
                        isEdit = false;
                        date = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+" "+ time.hour+":"+time.minute+":"+time.second ;
                        prov.editItem(id.toString(),Float.parseFloat(valueText.getText().toString()),date,prov.getCategoryId(category));
                    }
                    else {

                        date = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+" "+ time.hour+":"+time.minute+":"+time.second ;
                        prov.addItem(Float.parseFloat(valueText.getText().toString()),date ,prov.getCategoryId(category),1);
                    }
                }
            });
            return rootView;
        }


    }

}

