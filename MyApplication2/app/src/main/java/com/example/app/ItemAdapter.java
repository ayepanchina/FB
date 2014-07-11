package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Анастасия on 01.04.14.
 */
public class ItemAdapter extends ArrayAdapter {

    List<ItemRow>   data;
    Context context;
    int layoutResID;
    private DatabaseProvider holderProvider;
    DBHelper dbOpen;

    public ItemAdapter(Context context, int layoutResourceId,List<ItemRow> data,DBHelper dbOpen) {
        super(context, layoutResourceId, data);

        this.data=data;
        this.context=context;
        this.layoutResID=layoutResourceId;
        this.dbOpen = dbOpen;
        this.holderProvider = new DatabaseProvider(dbOpen);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewsHolder holder = null;
        View row = convertView;
        holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResID, parent, false);

            holder = new NewsHolder();

            holder.itemName = (TextView)row.findViewById(R.id.example_itemname);
            holder.icon=(ImageView)row.findViewById(R.id.example_image);
            holder.button1=(Button)row.findViewById(R.id.swipe_button1);
            holder.button2=(Button)row.findViewById(R.id.swipe_button2);
            holder.button3=(Button)row.findViewById(R.id.swipe_button3);
            row.setTag(holder);
        }
        else
        {
            holder = (NewsHolder)row.getTag();
        }

        final ItemRow itemdata = data.get(position);
        final int itemPosition = position;
        holder.itemName.setText(itemdata.getItemName());
        holder.icon.setImageDrawable(itemdata.getIcon());


        holder.button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


              //  Toast.makeText(context, "ID= " + id,Toast.LENGTH_SHORT).show();
                removeItem(itemPosition);

               setNotifyOnChange(true);
            notifyDataSetChanged();

                //Toast.makeText(context, "Button 1 Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        holder.button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "Button 2 Clicked",Toast.LENGTH_SHORT).show();
            }
        });

        holder.button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "Button 3 Clicked",Toast.LENGTH_SHORT);
            }
        });

        return row;

    }

    public void removeItem(int position) {
        ItemRow itemdata = data.get(position);
        long id= holderProvider.getCategoryId(itemdata.getItemName());
        data.remove(position);
        holderProvider.delCategory(id);
    }





    static class NewsHolder{

        TextView itemName;
        ImageView icon;
        Button button1;
        Button button2;
        Button button3;
    }



}