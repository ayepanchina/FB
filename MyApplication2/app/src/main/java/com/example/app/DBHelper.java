package com.example.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Анастасия on 26.03.14.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Budgets";
    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String ITEM_TABLE_NAME = "item";
    public static final String ITEM_ADVANCED_TABLE_NAME = "item_advanced";

    public static final String TABLE_ID = "_id";
    public static final String CATEGORY_TYPE = "cat_type";
    public static final String CATEGORY_NAME = "cat_name";
    public static final String CATEGORY_PLAN_VALUE = "cat_plan_value";

    public static final String ITEM_CATEGORY_ID = "cat_id";
    public static final String ITEM_VALUE = "value";
    public static final String ITEM_DATE = "date";
    public static final String ITEM_TYPE = "item_type";

    public static final String ADVANCED_REPEAT_TYPE = "repeat_type";
    public static final String ADVANCED_NOTE = "note";
    public static final String ADVANCED_ATTACH = "attach";
    public static final String ADVANCED_REMINDER_TYPE = "reminder_type";

    private static final String CATEGORY_TABLE_CREATE = "create table " + CATEGORY_TABLE_NAME + " ( " +
            TABLE_ID + " integer primary key autoincrement, " +
            CATEGORY_TYPE + " integer, " +
            CATEGORY_PLAN_VALUE + " float, " +
            CATEGORY_NAME + " text);";

    private static final String ITEM_TABLE_CREATE = "create table " + ITEM_TABLE_NAME + " ( " +
            TABLE_ID+" integer primary key autoincrement, " +
            ITEM_CATEGORY_ID + " integer, " +
            ITEM_VALUE + " float, " +
            ITEM_DATE + " date, " +
            ITEM_TYPE + " text);";

    private static final String ITEM_ADVANCED_TABLE_CREATE = "create table " + ITEM_ADVANCED_TABLE_NAME + " ( " +
            TABLE_ID + " integer primary key autoincrement, " +
            ADVANCED_REPEAT_TYPE + " integer, " +
            ADVANCED_REMINDER_TYPE + " integer, " +
            ADVANCED_NOTE + " text, " +
            ADVANCED_ATTACH + " text);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CATEGORY_TABLE_CREATE);
         sqLiteDatabase.execSQL(ITEM_TABLE_CREATE);
        sqLiteDatabase.execSQL(ITEM_ADVANCED_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
