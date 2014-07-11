package com.example.app;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * Created by Вадим on 20.05.14.
 */
public class TransactionDialogFragment extends DialogFragment {

    String catId;
    String category="";
    Calendar cal;
    boolean isEdit=false;
    float value;
    String itemDate;
    public static TextView mDateTextView;
    private DatabaseProvider provider;
    static DatabaseProvider prov;

    static TransactionDialogFragment newInstance(String catId, DatabaseProvider provider) {
        TransactionDialogFragment f = new TransactionDialogFragment();

        prov = provider;
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("catId", Integer.valueOf(catId));
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catId = String.valueOf(getArguments().getInt("catId"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(getString(R.string.add_transaction));
        View v = inflater.inflate(R.layout.transaction_fragment_dialog, container, false);
        mDateTextView = (TextView) v.findViewById(R.id.date);
        final TextView valueText = (TextView) v.findViewById(R.id.editValue);

        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        cal = Calendar.getInstance();
        Date date1=null;
        List<String> array_spinner=null;
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        final Button btn = (Button) v.findViewById(R.id.btnAddTransaction);
        btn.setEnabled(false);
        // final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        array_spinner = prov.getAllCategoriesListStartFromCatId(catId);
        valueText.setText("");
        valueText.addTextChangedListener(new TextWatcher() {
            boolean flag = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!str.startsWith(".") && !str.endsWith(".") && !str.equals("")) {
                    flag = true;
                }
                else {
                    flag = false;
                }
                btn.setEnabled(flag);
            }
        });

        mDateTextView.setText(String.format("%1$tb %1$td,  %1$tY",cal.getTime()));

        final Spinner spinner = (Spinner) v.findViewById(R.id.spinnerCategory);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time time = new Time();
                time.setToNow();
                String date = "";
                date = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH)+" "+ time.hour+":"+time.minute+":"+time.second ;
                prov.addItem(Float.parseFloat(valueText.getText().toString()),date ,prov.getCategoryId(category),1);
                MainLoader.getInstance().forceLoad();
                getDialog().dismiss();
            }
        });
        return v;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
