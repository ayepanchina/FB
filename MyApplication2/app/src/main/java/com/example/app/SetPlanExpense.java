package com.example.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SetPlanExpense extends Activity {
    static Calendar cal;
    static DatabaseProvider prov;
    static  Float bigExpense;
    DBHelper dbOpen;
    SQLiteDatabase base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_plan_expense);
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
        getMenuInflater().inflate(R.menu.set_plan_expense, menu);
        return true;
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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
       static TextView planDate;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_set_plan_expense, container, false);
            planDate = (TextView) rootView.findViewById(R.id.plandate);
            Button planButton = (Button)rootView.findViewById(R.id.button);
            final EditText planValue = (EditText)rootView.findViewById(R.id.editTextFood);
            final EditText planNote = (EditText)rootView.findViewById(R.id.editTextBill);
            planButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bigExpense = Float.parseFloat(planValue.getText().toString());
                    prov.addItem(bigExpense,"bigExpense",cal.getTime(),planNote.getText().toString());
                    bigExpense = prov.getPlanValue(null,"bigExpense");
                    Calendar today = Calendar.getInstance();
                    long diff = cal.getTimeInMillis() - today.getTimeInMillis(); //result in millis
                    long days = diff / (24 * 60 * 60 * 1000);
                    double month = days * 0.0328549112;
                    float pln=getAllPlanOutcomesValues();
                    float b = getAllPlanIncomesValues();
                    float diffPlanIncomes= (float) (month*getAllPlanIncomesValues());
                    float diffPlanOutcomes = (float) (month*getAllPlanOutcomesValues());
                    float reserve = diffPlanIncomes - diffPlanOutcomes;
                    Toast.makeText(getActivity(), reserve + getString(R.string.reserved), Toast.LENGTH_LONG).show();
                    if (bigExpense>reserve)
                    {
                        float newPlanIncomes = (float) (bigExpense/month + getAllPlanOutcomesValues());
                        float newPlanOutcomes = (float) (getAllPlanIncomesValues() - bigExpense/month);
                        Intent intent=new Intent();
                        intent.putExtra("reserve", reserve);
                        intent.putExtra("needed", bigExpense-reserve);
                        intent.putExtra("newIncomes", newPlanIncomes);
                        intent.putExtra("newOutcomes", newPlanOutcomes);
                        intent.setClass(getActivity(),PlanFailed.class);
                        startActivity(intent);
                       /* Toast.makeText(getActivity(), bigExpense-reserve + " needed", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), " My advices", Toast.LENGTH_LONG).show();
                       float newPlanIncomes = (float) (bigExpense/month + getAllPlanOutcomesValues());
                        Toast.makeText(getActivity(), "1st Incomes should be not less than " + newPlanIncomes +" per month", Toast.LENGTH_LONG).show();
                        float newPlanOutcomes = (float) (getAllPlanIncomesValues() - bigExpense/month);
                        Toast.makeText(getActivity(), "2nd Outcomes should be reduced to " + newPlanOutcomes +" per month", Toast.LENGTH_LONG).show();
*/
                    }
                    else {
                        Intent intent=new Intent();
                        intent.putExtra("reserve", reserve);
                        intent.setClass(getActivity(),PlanSuccessfull.class);
                        startActivity(intent);
                       // Toast.makeText(getActivity(), " You are lucky one", Toast.LENGTH_LONG).show();

                    }
                    Log.i("myLog", days+" = " + month);
                    Log.i("myLog", diffPlanIncomes+ " " + diffPlanOutcomes);
                }
            });
            cal = Calendar.getInstance();
            planDate.setText((String.format("%1$tb %1$td,  %1$tY",cal.getTime())));
            return rootView;
        }
        float getAllPlanIncomesValues(){

            return prov.getAllPlanIncomes();
        }
        float getAllPlanOutcomesValues(){

            return prov.getAllPlanExpense();
        }
    }

}
