package com.example.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeToRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class AllCategoriesActivity extends Activity {
    static SwipeListView swipelistview;
    static ItemAdapter adapter;
    static List<ItemRow> itemData;
    static DatabaseProvider prov;
   static DBHelper dbOpen;
    SQLiteDatabase base;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        dbOpen = new DBHelper(this);
        base = dbOpen.getWritableDatabase();

        this.prov=new DatabaseProvider(dbOpen);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity3, menu);
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
    public void reload()
    {
  onRestart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i=new Intent(this,AllCategoriesActivity.class);
        startActivity(i);
        this.finish();

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public  class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main3, container, false);
            swipelistview=(SwipeListView)rootView.findViewById(R.id.example_swipe_lv_list);
            itemData=new ArrayList<ItemRow>();
            adapter=new ItemAdapter(getActivity(),R.layout.custom_row,itemData,dbOpen);

            swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
                @Override
                public void onOpened(int position, boolean toRight) {
                }

                @Override
                public void onClosed(int position, boolean fromRight) {
                }

                @Override
                public void onListChanged() {
                }

                @Override
                public void onMove(int position, float x) {
                }

                @Override
                public void onStartOpen(int position, int action, boolean right) {
                    Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
                }

                @Override
                public void onStartClose(int position, boolean right) {
                    Log.d("swipe", String.format("onStartClose %d", position));
                }

                @Override
                public void onClickFrontView(int position) {
                    Log.d("swipe", String.format("onClickFrontView %d", position));

                    swipelistview.openAnimate(position); //when you touch front view it will open
                    //swipelistview.setAdapter(adapter);
                }

                @Override
                public void onClickBackView(int position) {
                    Log.d("swipe", String.format("onClickBackView %d", position));
                    //reload();
                    swipelistview.closeAnimate(position);//when you touch back view it will close
                }

                @Override
                public void onDismiss(int[] reverseSortedPositions) {

                }

            });
            swipelistview.setRefreshListener(new SwipeToRefreshListener() {
                @Override
                public void onRefresh() {
                    Toast.makeText(getActivity(),"sdsdsd",Toast.LENGTH_LONG).show();
                }
            });
                    //These are the swipe listview settings. you can change these
                    //setting as your requirement
                    swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are five swiping modes
            swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_DISMISS); //there are four swipe actions
            swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
            swipelistview.setOffsetLeft(convertDpToPixel(0f)); // left side offset
            swipelistview.setOffsetRight(convertDpToPixel(80f)); // right side offset
            swipelistview.setAnimationTime(10); // Animation time
            swipelistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress
            ArrayList<String>  cat=prov.getAllCategoriesList();
            for(int i=0;i<cat.size();i++)
            {
                itemData.add(new ItemRow(cat.get(i),getResources().getDrawable(R.drawable.ic_launcher) ));

            }

            adapter.notifyDataSetChanged();
            swipelistview.setAdapter(adapter);
            return rootView;
        }
        public int convertDpToPixel(float dp) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            float px = dp * (metrics.densityDpi / 160f);
            return (int) px;
        }

    }

}
