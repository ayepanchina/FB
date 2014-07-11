package com.example.app;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Balance.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Balance#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Balance extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static DatabaseProvider prov;
    DBHelper dbOpen;
    SQLiteDatabase base;

    static Calendar cal;
    static Calendar cal2;
    static boolean isFrom=false;
    static boolean isTo=false;
    static TextView mDateFrom;
    static TextView mDateTo;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Balance.
     */

    public Balance(DBHelper dbOpen) {
        this.dbOpen = dbOpen;
        this.prov = new DatabaseProvider(dbOpen);
         // Required empty public constructor
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
        final View v = inflater.inflate(R.layout.fragment_balance, container, false);
        assert v != null;
        final BarGraph graph = (BarGraph)v.findViewById(R.id.bargraph);
        mDateFrom = (TextView) v.findViewById(R.id.textViewDateFrom2);
        mDateTo = (TextView) v.findViewById(R.id.textViewDateTo2);

        cal = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        Date date1 = null;
        List<String> array_spinner=null;
        mDateFrom.setText(String.format("%1$tb %1$td,  %1$tY", cal.getTime()));
        mDateTo.setText(String.format("%1$tb %1$td,  %1$tY", cal2.getTime()));
        mDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransactionHistory.isFrom=false;
                TransactionHistory.isTo=false;
                isFrom=true;
                isTo=false;
                showDatePickerDialog(view);
            }
        });
        mDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionHistory.isFrom=false;
                TransactionHistory.isTo=false;
                isFrom=false;
                isTo=true;
                showDatePickerDialog(view);
            }
        });
        final CheckBox checkBoxFromTo = (CheckBox)v.findViewById(R.id.checkBoxFromTo2);
        checkBoxFromTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxFromTo.isChecked()) {
                    ArrayList<Bar> points = new ArrayList<Bar>();
                    Bar d = new Bar();
                    d.setColor(Color.parseColor("#99CC00"));
                    d.setName(getString(R.string.income));
                    d.setValue(prov.getAllIncomes(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.YEAR) + "-" + (cal2.get(Calendar.MONTH) + 1) + "-" + cal2.get(Calendar.DAY_OF_MONTH)));
                    Bar d2 = new Bar();
                    d2.setColor(Color.parseColor("#FFBB33"));
                    d2.setName(getString(R.string.outcome));
                    d2.setValue(prov.getAllOutcomes(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.YEAR) + "-" + (cal2.get(Calendar.MONTH) + 1) + "-" + cal2.get(Calendar.DAY_OF_MONTH)
                    ));
                    points.add(d);
                    points.add(d2);

                    graph.setShouldUpdate(true);
                    graph.setUnit("₴");
                    graph.appendUnit(true);
                    graph.setBars(points);
                }
            }
        });
        final CheckBox checkBoxCurrentMonth = (CheckBox)v.findViewById(R.id.checkBoxCurrentMonth2);
        checkBoxCurrentMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Time time = new Time();
                time.setToNow();
                if (checkBoxCurrentMonth.isChecked()){
                    int month = time.month;
                    int year = time.year;
                    ArrayList<Bar> points = new ArrayList<Bar>();
                    Bar d = new Bar();
                    d.setColor(Color.parseColor("#99CC00"));
                    d.setName(getString(R.string.income));
                    d.setValue(prov.getAllIncomes(year+"-"+(month+1)+"-"+ "1",year+"-"+(month+2)+"-"+"1"));
                    Bar d2 = new Bar();
                    d2.setColor(Color.parseColor("#FFBB33"));
                    d2.setName(getString(R.string.outcome));
                    d2.setValue(prov.getAllOutcomes(year + "-" + (month + 1) + "-" + "1", year + "-" + (month + 2) + "-" + "1"));
                    points.add(d);
                    points.add(d2);

                    graph.setShouldUpdate(true);
                    graph.setUnit("₴");
                    graph.appendUnit(true);
                    graph.setBars(points);
                }
            }
        });

        final CheckBox checkBoxCurrentYear = (CheckBox)v.findViewById(R.id.checkBoxCurrentYear2);
        checkBoxCurrentYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Time time = new Time();
                time.setToNow();
                if (checkBoxCurrentYear.isChecked()) {
                    int year = time.year;
                    ArrayList<Bar> points = new ArrayList<Bar>();
                    Bar d = new Bar();
                    d.setColor(Color.parseColor("#99CC00"));
                    d.setName(getString(R.string.income));
                    d.setValue(prov.getAllIncomes(year + "-1-1", (year + 1) + "-1-1"));
                    Bar d2 = new Bar();
                    d2.setColor(Color.parseColor("#FFBB33"));
                    d2.setName(getString(R.string.outcome));
                    d2.setValue(prov.getAllOutcomes(year + "-1-1", (year + 1) + "-1-1"));
                    points.add(d);
                    points.add(d2);

                    graph.setShouldUpdate(true);
                    graph.setUnit("₴");
                    graph.appendUnit(true);
                    graph.setBars(points);
                }
            }
        });

        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d = new Bar();
        d.setColor(Color.parseColor("#99CC00"));
        d.setName(getString(R.string.income));
        d.setValue(getAllIncomesValues());
        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#FFBB33"));
        d2.setName(getString(R.string.outcome));
        d2.setValue(getAllOutcomesValues());
        points.add(d);
        points.add(d2);

        assert graph != null;
        graph.setUnit("₴");
        graph.appendUnit(true);
        graph.setBars(points);
        return v;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    float getAllIncomesValues(){

       return prov.getAllIncomes();
    }
    float getAllOutcomesValues(){

        return prov.getAllOutcomes();
    }

}
