package com.example.app;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Вадим on 18.05.14.
 */
public class ExpensePlanAdapter extends SimpleCursorAdapter {

    static class ViewHolder {
        TextView catName;
        EditText editValue;
        CheckBox checkBox;
        int position;
    }

    List<ExpensePlanValue> expensePlanValueList;
    private Context context;
    private Cursor cursor;
    private ViewGroup parent;

    public ExpensePlanAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        expensePlanValueList = new ArrayList<ExpensePlanValue>();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        String catName = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));
        //String date = c.getString(c.getColumnIndex(DBHelper.ITEM_DATE));
        final float plan = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_PLAN_VALUE));
        //TextView textCatName = holder.catName;
        //EditText editTextValue = holder.editValue;

        //textCatName.setText(catName);
        //editTextValue.setText(String.valueOf(plan));

        viewHolder.position = cursor.getPosition();
        viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxAccept);
        viewHolder.catName = (TextView) view.findViewById(R.id.textCategory);
        viewHolder.editValue = (EditText) view.findViewById(R.id.editTextValue);
        view.setTag(viewHolder);

        viewHolder.catName.setText(catName);
        viewHolder.editValue.setText(String.valueOf(plan));

        final ExpensePlanValue planValue = new ExpensePlanValue(cursor.getInt(0), plan);
        expensePlanValueList.add(planValue);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                planValue.setEdited(isChecked);
            }
        });

        viewHolder.editValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                viewHolder.checkBox.setChecked(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();

                if (str.equals(String.valueOf(plan))) {
                    viewHolder.checkBox.setChecked(false);
                }

                // Format value
                if (str.startsWith(".")) {
                    str = str.replaceFirst(".", "0.");
                    viewHolder.editValue.setText(str);
                    viewHolder.editValue.setSelection(str.length());
                }

                if (str.endsWith(".")) {
                    str += "0";
                }

                float value = Float.parseFloat(str);
                planValue.setValue(value);
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final ViewHolder viewHolder = new ViewHolder();
        String catName = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));
        //String date = c.getString(c.getColumnIndex(DBHelper.ITEM_DATE));
        final float plan = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_PLAN_VALUE));

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.plan_expense_item, parent, false);


        view.setTag(viewHolder);
        bindView(view, context, cursor);

        return view;
    }

    public List<ExpensePlanValue> getExpensePlanValueList() {
        return this.expensePlanValueList;
    }
}
