package com.example.app;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

/**
 * Created by Вадим on 18.05.14.
 */
public class SummaryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    DBHelper dbOpen;
    SQLiteDatabase db;
    DatabaseProvider provider;
    ExpenseSummaryAdapter listAdapter;
    Cursor cursor;

    private static final String[] FROM = {DBHelper.CATEGORY_NAME,DBHelper.CATEGORY_PLAN_VALUE, "sum(" + DBHelper.ITEM_VALUE + ")"};
    private static final int[] TO = {R.id.textName,R.id.itemDate};

    public SummaryFragment(DBHelper dbOpen) {
        this.dbOpen = dbOpen;
        this.provider = new DatabaseProvider(dbOpen);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = provider.getExpenseCategories();
        listAdapter = new ExpenseSummaryAdapter(getActivity(), R.layout.summary_listitem, getCursor(), FROM, TO, 0);
        // To make button calls to this fragment
        listAdapter.setFragment(this);
        getLoaderManager().initLoader(0, savedInstanceState, this);
        setListAdapter(listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        ListView listView=(ListView) view.findViewById(android.R.id.list);

        return view;

    }

    public void showTransactionDialog(String catId) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TransactionDialogFragment.newInstance(catId, provider);
        newFragment.show(ft, "dialog");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MainLoader(getActivity(), provider, 0);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        getAdapter().swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public CursorAdapter getAdapter() {
        return this.listAdapter;
    }

    public Cursor getCursor() {
        return this.cursor;
    }
}
