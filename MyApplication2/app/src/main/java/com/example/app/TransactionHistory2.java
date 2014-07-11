package com.example.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class TransactionHistory2 extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private static final String myLog="myLog";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DBHelper dbOpen;
    SQLiteDatabase db;
    DatabaseProvider holderProvider;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;
    private static final String[] FROM = {DBHelper.CATEGORY_NAME};
    private static final int[] TO = {R.id.textName};
    private static final int CM_DELETE_ID = 1;
    private static final int CM_Edit_ID = 2;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionHistory.
     */
    // TODO: Rename and change types and number of parameters
   /* public static TransactionHistory newInstance(String param1, DBHelper dbOpen) {
        TransactionHistory fragment = new TransactionHistory(dbOpen);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;

    }*/
    public TransactionHistory2(DBHelper dbOpen) {
        this.dbOpen = dbOpen;
        this.holderProvider = new DatabaseProvider(dbOpen);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        cursor = holderProvider.getAllCategoriesCursor();

        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.categoty_row, cursor, FROM, TO, 0);
        setListAdapter(simpleCursorAdapter);

        Log.i(myLog, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(myLog, "onCreateView");

       OnClickListener listnr=new OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditCategory.isEdit=false;
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddEditCategory.class);
                startActivity(intent);
            }
        } ;
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        Button button = (Button) view.findViewById(R.id.addCat);
        button.setOnClickListener(listnr);

        Button bt2=(Button)view.findViewById(R.id.allcat);
        bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), AllCategoriesActivity.class);
                startActivity(intent2);
            }
        });
        ListView listView=(ListView) view.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return view;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
       // super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
        menu.add(0, CM_Edit_ID, 0, R.string.edit);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
      if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            holderProvider.delCategory(acmi.id);
          onCreate(new Bundle());
            // обновляем курсор
            cursor.requery();
            return true;
        }
        if (item.getItemId() == CM_Edit_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            long some=acmi.id;
            //Toast.makeText(getActivity(), "5 "+some, Toast.LENGTH_LONG).show();
           Intent intent = new Intent();
            intent.putExtra("some_id", some);
            intent.setClass(getActivity(), AddEditCategory.class);
            startActivity(intent);
            // обновляем курсор
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(myLog, "onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(myLog, "onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(myLog, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(myLog, "onDestroyView");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(myLog, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(myLog, "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(myLog, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(myLog, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(myLog, "onStop");
    }


}
