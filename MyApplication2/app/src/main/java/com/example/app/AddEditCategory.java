package com.example.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEditCategory extends Activity {


    static DatabaseProvider prov;
    DBHelper dbOpen;
    SQLiteDatabase base;
    Cursor cursor;
    static String name="";
    static String type="";
    static Long id;
    static boolean isEdit=false;
    static Long catType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();

       // String id = intent.getStringExtra("some_id");
      id= intent.getLongExtra("some_id",-1);
        //String lName = intent.getStringExtra("lname");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        dbOpen = new DBHelper(this);
        base = dbOpen.getWritableDatabase();

       this.prov=new DatabaseProvider(dbOpen);
        if (id!=-1) {
            isEdit=true;
       cursor=prov.getCategoryById(id);
       if (cursor.moveToFirst()){
           name=cursor.getString(cursor.getColumnIndex("cat_name"));
           catType=cursor.getLong(cursor.getColumnIndex("cat_type"));
           type=cursor.getString(cursor.getColumnIndex("cat_type"));
       }

cursor.close();
        }
        Toast.makeText(AddEditCategory.this, name+ "+"+type+" +"+isEdit, Toast.LENGTH_LONG).show();
    }

     OnClickListener lis=new OnClickListener() {
        @Override
        public void onClick(View view) {
            //
        }
    } ;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_add_edit_category, container, false);
            final Button button2 = (Button) rootView.findViewById(R.id.button4);
            final EditText ad1=(EditText)rootView.findViewById(R.id.editTextFood);
            final EditText ad2=(EditText)rootView.findViewById(R.id.editTextBill);
            final String array_spinner[] = new String[2];

            if (isEdit){

                if (catType==0){
                    array_spinner[0] = getString(R.string.income);
                    array_spinner[1] = getString(R.string.expense);
                } else if (catType==1){

                    array_spinner[0] = getString(R.string.expense);
                    array_spinner[1] = getString(R.string.income);
                }
            } else {
                name = "";
                type = "";
                array_spinner[0] = getString(R.string.income);
                array_spinner[1] = getString(R.string.expense);
            }
            ad1.setText(name);
            //ad2.setText(type);
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, array_spinner);
            spinner.setAdapter(adapter);

           spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   long id=spinner.getSelectedItemId();
                   catType=spinner.getSelectedItemId();
                   Toast.makeText(getActivity(), catType + getString(R.string.position) +id, Toast.LENGTH_LONG).show();
               }

               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {

               }
           });
            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isEdit) {
                        isEdit = false;
                        name = "";
                        type = "";
                        Toast.makeText(getActivity(), id + ad1.getText().toString() + ad2.getText().toString(), Toast.LENGTH_LONG).show();
                        prov.editCategory(id.toString(), ad1.getText().toString(), catType);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        startActivity(intent);

                    } else {
                        prov.addCategory(ad1.getText().toString(), catType);

                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            });

            return rootView;
        }
    }

}
