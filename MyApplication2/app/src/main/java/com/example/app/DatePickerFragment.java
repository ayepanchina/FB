package com.example.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Анастасия on 14.04.14.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

  String activityName="";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
    activityName = getActivity().getLocalClassName();
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        if (activityName.equals(AddEditItem.class.getSimpleName()))
        {
            AddEditItem.cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            AddEditItem.PlaceholderFragment.mDateTextView.setText(String.format("%1$tb %1$td,  %1$tY", AddEditItem.cal.getTime()));

        } else
        if (activityName.equals(SetPlanExpense.class.getSimpleName()))
        {

            SetPlanExpense.cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            SetPlanExpense.PlaceholderFragment.planDate.setText(String.format("%1$tb %1$td,  %1$tY", SetPlanExpense.cal.getTime()));
        } else
        if (activityName.equals(MainActivity.class.getSimpleName()) )
        {
            if (TransactionHistory.isFrom)
            {
                TransactionHistory.cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                TransactionHistory.mDateFrom.setText(String.format("%1$tb %1$td,  %1$tY", TransactionHistory.cal.getTime()));
            }
            else if (TransactionHistory.isTo)
            {
                TransactionHistory.cal2 = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                TransactionHistory.mDateTo.setText(String.format("%1$tb %1$td,  %1$tY", TransactionHistory.cal2.getTime()));
            }
            if (Balance.isFrom)
            {
                Balance.cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Balance.mDateFrom.setText(String.format("%1$tb %1$td,  %1$tY", Balance.cal.getTime()));
            }
            else if (Balance.isTo)
            {
                Balance.cal2 = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Balance.mDateTo.setText(String.format("%1$tb %1$td,  %1$tY", Balance.cal2.getTime()));
            }
        }
    }
}