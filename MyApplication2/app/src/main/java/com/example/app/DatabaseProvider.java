package com.example.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Анастасия on 26.03.14.
 */
public class DatabaseProvider {
    private DBHelper dbHelp;
    private SQLiteDatabase db;
    public DatabaseProvider(DBHelper dbHelp) {
        this.dbHelp = dbHelp;
    }
    public Cursor getAllCategoriesCursor() {
        String query = "select " +
                DBHelper.TABLE_ID + ", " +
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.CATEGORY_TABLE_NAME + "; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public Cursor getSumOfFourExpenseValues() {
        String query = "select sum(" +DBHelper.ITEM_VALUE+"), "+
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_TYPE+" = 1"+
                " AND "+DBHelper.CATEGORY_TYPE+" = 1 "+
                " group by "+DBHelper.ITEM_CATEGORY_ID+
                " order by "+DBHelper.ITEM_VALUE+" desc limit 4; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getSumExpenseValues() {
        String query = "select sum(" +DBHelper.ITEM_VALUE+"), "+
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_TYPE+" = 1"+
                " AND "+DBHelper.CATEGORY_TYPE+" = 1; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getExpenseCategories() {
        String query = "select " +
                DBHelper.CATEGORY_TABLE_NAME + "." + DBHelper.TABLE_ID + " , " +
                DBHelper.ITEM_CATEGORY_ID +", " +
                DBHelper.CATEGORY_NAME+ ", "
                + DBHelper.CATEGORY_PLAN_VALUE
                +", sum(" +DBHelper.ITEM_VALUE+") " +" FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_TYPE+" = 1"+
                " AND "+DBHelper.CATEGORY_TYPE+" = 1 "+
                " group by "+DBHelper.ITEM_CATEGORY_ID+";";


        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public ArrayList<String> getAllCategoriesList() {
        ArrayList<String> all=new ArrayList<String>();
        String query = "select " +
                DBHelper.TABLE_ID + ", " +
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.CATEGORY_TABLE_NAME + "; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()){
            do
            {
                String name=cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));
                all.add(name);
            }
            while(cursor.moveToNext());
        }
        return all;
    }

    public ArrayList<String> getAllCategoriesListStartFromCatId(String catId) {
        ArrayList<String> all=new ArrayList<String>();
        String query = "select " +
                DBHelper.TABLE_ID + ", " +
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.CATEGORY_TABLE_NAME + "; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()){
            do
            {
                String name=cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));
                if (cursor.getString(cursor.getColumnIndex(DBHelper.TABLE_ID)).equals(catId)) {
                    all.add(0, name);
                }
                else {
                    all.add(name);
                }
            }
            while(cursor.moveToNext());
        }
        return all;
    }

    public ArrayList<String> getAllCategoriesListForEditItem(int id) {
        ArrayList<String> all=new ArrayList<String>();
        String query = "select " +
                DBHelper.TABLE_ID + ", " +
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.CATEGORY_TABLE_NAME + " where " +
                DBHelper.TABLE_ID +" = " + id + "; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()){
            do
            {
                String name=cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));
                all.add(name);
            }
            while(cursor.moveToNext());
        }
        String query2 = "select " +
                DBHelper.TABLE_ID + ", " +
                DBHelper.CATEGORY_NAME + " FROM " +
                DBHelper.CATEGORY_TABLE_NAME + " where " +
                DBHelper.TABLE_ID +" != " + id + "; ";
        Cursor cursor2 = db.rawQuery(query2, null);
        if (cursor2 != null && cursor2.moveToFirst()){
            do
            {
                String name=cursor2.getString(cursor2.getColumnIndex(DBHelper.CATEGORY_NAME));
                all.add(name);
            }
            while(cursor2.moveToNext());
        }
        db.close();

        return all;
    }

    public void addCategory(String name, Long type) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CATEGORY_TYPE, type);
        cv.put(DBHelper.CATEGORY_NAME, name);
        db.insert(DBHelper.CATEGORY_TABLE_NAME, null, cv);
        db.close();
    }
    public void editCategory(String id,String name, Long type) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CATEGORY_TYPE, type);
        cv.put(DBHelper.CATEGORY_NAME, name);
        db.update(DBHelper.CATEGORY_TABLE_NAME, cv,  DBHelper.TABLE_ID + " = ?",
                new String[] { id });
        db.close();
    }
    public int getCategoryId(String name) {
        db = dbHelp.getReadableDatabase();
        String query = "select " +
                DBHelper.TABLE_ID + " from " +
                DBHelper.CATEGORY_TABLE_NAME + " where " +
                DBHelper.CATEGORY_NAME + " LIKE " + "\""+ name +
                "\""+" ;" ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getInt(0);
        return 0;
    }
    public Cursor getCategoryById(long id) {
        db = dbHelp.getReadableDatabase();
        String query = "select " +
                DBHelper.TABLE_ID + ", "+
                DBHelper.CATEGORY_NAME + ", "+
                DBHelper.CATEGORY_TYPE +
                " from " +
                DBHelper.CATEGORY_TABLE_NAME +
                " where " + DBHelper.TABLE_ID + " = " + id + " ;";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
    // удалить запись из DB_TABLE
    public void delCategory(long id) {
        db.delete(dbHelp.CATEGORY_TABLE_NAME, dbHelp.TABLE_ID + " = " + id, null);
    }
    public Cursor getAllItemsCursor() {
        String query = "select " +
                DBHelper.ITEM_TABLE_NAME + "."+DBHelper.TABLE_ID + ", " +
                DBHelper.ITEM_VALUE + ", " +
                DBHelper.ITEM_DATE + ", " +
                DBHelper.ITEM_CATEGORY_ID + ", " +
                DBHelper.CATEGORY_TYPE + " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_TYPE+" = 1; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    public void addItem(Float value, String date, int catId, int type) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ITEM_VALUE, value);
        cv.put(DBHelper.ITEM_DATE, String.valueOf(date));
        cv.put(DBHelper.ITEM_CATEGORY_ID, catId);
        cv.put(DBHelper.ITEM_TYPE, type);
        ContentValues cv2 = new ContentValues();
        cv2.put(DBHelper.ADVANCED_NOTE, "not yet");
        db.insert(DBHelper.ITEM_TABLE_NAME, null, cv);
        db.insert(DBHelper.ITEM_ADVANCED_TABLE_NAME, null, cv2);
        db.close();
    }
    public void addItem(Float value, String type) {
        db = dbHelp.getWritableDatabase();
        String query = "select *" +
                " from " +
                DBHelper.ITEM_TABLE_NAME +
                " where " + DBHelper.ITEM_TYPE + " = '" + type + "' ;";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
           editItem(value,type);
        }
        else
        {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ITEM_VALUE, value);
        cv.put(DBHelper.ITEM_TYPE, type);
        ContentValues cv2 = new ContentValues();
        cv2.put(DBHelper.ADVANCED_NOTE, "not yet");
        db.insert(DBHelper.ITEM_TABLE_NAME, null, cv);
        db.insert(DBHelper.ITEM_ADVANCED_TABLE_NAME, null, cv2);
        }
        db.close();
    }
    public void addItem(Float value, String type, Date date, String note) {
        db = dbHelp.getWritableDatabase();
        String query = "select *" +
                " from " +
                DBHelper.ITEM_TABLE_NAME +
                " where " + DBHelper.ITEM_TYPE + " = '" + type + "' ;";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            editItem(cursor.getString(cursor.getColumnIndex(DBHelper.TABLE_ID)),value,type,note,date);
        }
        else
        {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.ITEM_VALUE, value);
            cv.put(DBHelper.ITEM_TYPE, type);
            ContentValues cv2 = new ContentValues();
            cv2.put(DBHelper.ADVANCED_NOTE, note);
            db.insert(DBHelper.ITEM_TABLE_NAME, null, cv);
            db.insert(DBHelper.ITEM_ADVANCED_TABLE_NAME, null, cv2);
        }
        db.close();
    }
    public float getPlanValue(Float value,String type) {
        db = dbHelp.getWritableDatabase();
        String query = "select *" +
                " from " +
                DBHelper.ITEM_TABLE_NAME +
                " where " + DBHelper.ITEM_TYPE + " = '" + type + "' ;";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            float a = cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_VALUE));
            db.close();
           return  cursor.getFloat(cursor.getColumnIndex(DBHelper.ITEM_VALUE));
        }
        else
        {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.ITEM_VALUE, value);
            cv.put(DBHelper.ITEM_TYPE, type);
            ContentValues cv2 = new ContentValues();
            cv2.put(DBHelper.ADVANCED_NOTE, "not yet");
            db.insert(DBHelper.ITEM_TABLE_NAME, null, cv);
            db.insert(DBHelper.ITEM_ADVANCED_TABLE_NAME, null, cv2);
            db.close();
            return value;
        }


    }
    public void editItem(Float value, String type) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ITEM_VALUE, value);
        cv.put(DBHelper.ITEM_TYPE, type);
        db.update(DBHelper.ITEM_TABLE_NAME, cv, DBHelper.ITEM_TYPE + " = ?",
                new String[]{type});
        db.close();
    }
    public void editItem(String id, Float value, String type,String note, Date date) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ITEM_VALUE, value);
        cv.put(DBHelper.ITEM_DATE, String.valueOf(date));
        cv.put(DBHelper.ITEM_TYPE, type);
        db.update(DBHelper.ITEM_TABLE_NAME, cv, DBHelper.ITEM_TYPE + " = ?",
                new String[]{type});
        ContentValues cv2 = new ContentValues();
        cv2.put(DBHelper.ADVANCED_NOTE, note);
        db.update(DBHelper.ITEM_ADVANCED_TABLE_NAME, cv2, DBHelper.TABLE_ID + " = ?",
                new String[]{id});
        db.close();
    }
    public void editItem(String id,Float value, String date, int catId) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.ITEM_VALUE, value);
        cv.put(DBHelper.ITEM_DATE, String.valueOf(date));
        cv.put(DBHelper.ITEM_CATEGORY_ID, catId);
        db.update(DBHelper.ITEM_TABLE_NAME, cv,  DBHelper.TABLE_ID + " = ?",
                new String[] { id });
        db.close();
    }
    public float getAllPlanExpense (){
        db = dbHelp.getReadableDatabase();
        String query = "select Sum(value) " +
                " FROM " +
                DBHelper.ITEM_TABLE_NAME +
                " where "+
                DBHelper.ITEM_TYPE + " != '1' " +
                " AND "+DBHelper.ITEM_TYPE+" != 'bigExpense' "+
                " AND "+DBHelper.ITEM_TYPE+" != 'income'; ";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getFloat(0);
        return 0;
    }
    public float getAllPlanIncomes (){
        db = dbHelp.getReadableDatabase();
        String query = "select Sum(value) " +
                " FROM " +
                DBHelper.ITEM_TABLE_NAME +
                " where "+
                DBHelper.ITEM_TYPE +" = 'income'; ";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getFloat(0);
        return 0;
    }
    public float getAllIncomes (){
        db = dbHelp.getReadableDatabase();
        String query = "select Sum(value) " +
                " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.CATEGORY_TYPE+" = 0; ";
        // "GROUP BY " + DBHelper.CATEGORY_TYPE + " ;";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getFloat(0);
        return 0;
    }
    public float getAllOutcomes (){
        db = dbHelp.getReadableDatabase();
        String query = "select Sum(value) " +
                " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.CATEGORY_TYPE+" = 1; ";
        // "GROUP BY " + DBHelper.CATEGORY_TYPE + " ;";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getFloat(0);
        return 0;
    }
    public Cursor getItemById(long id) {
        db = dbHelp.getReadableDatabase();
        String query = "select " +
                DBHelper.ITEM_TABLE_NAME + "."+DBHelper.TABLE_ID + ", "+
                DBHelper.ITEM_VALUE + ", "+
                DBHelper.ITEM_TYPE + ", "+
                DBHelper.ITEM_CATEGORY_ID + ", "+
                DBHelper.ITEM_DATE + ", "+
                DBHelper.ADVANCED_REMINDER_TYPE+ ", "+
                DBHelper.ADVANCED_ATTACH + ", "+
                DBHelper.ADVANCED_REPEAT_TYPE + ", "+
                DBHelper.ADVANCED_NOTE +
                " from " +
                DBHelper.ITEM_TABLE_NAME + ", " + DBHelper.ITEM_ADVANCED_TABLE_NAME +
                " where " + DBHelper.ITEM_TABLE_NAME + "."+DBHelper.TABLE_ID + " = " + id + " ;";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
    // удалить запись из DB_TABLE
    public void delItem(long id) {
        db.delete(dbHelp.ITEM_TABLE_NAME, dbHelp.TABLE_ID + " = " + id, null);
        db.delete(dbHelp.ITEM_ADVANCED_TABLE_NAME, dbHelp.TABLE_ID + " = " + id, null);
    }

    public void setPlanValueToCategoryId(String id, Float value) {
        db = dbHelp.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CATEGORY_PLAN_VALUE, value);
        db.update(DBHelper.CATEGORY_TABLE_NAME, cv, DBHelper.TABLE_ID + " = ?",
                new String[]{id});
         db.close();
    }

    public float getAllIncomes (String from,String to){
        db = dbHelp.getReadableDatabase();
        String query = "select Sum(value) " +
                " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_DATE + " BETWEEN '" + from + "' AND '" + to+
                "' AND "+DBHelper.CATEGORY_TYPE+" = 0; ";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getFloat(0);
        return 0;
    }
    public float getAllOutcomes (String from,String to){
        db = dbHelp.getReadableDatabase();
        String query = "select Sum(value) " +
                " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_DATE + " BETWEEN '" + from + "' AND '" + to+
                "' AND "+DBHelper.CATEGORY_TYPE+" = 1; ";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getFloat(0);
        return 0;
    }
    public Cursor getAllItemsCursor(String from, String to) {
        String query = "select " +
                DBHelper.ITEM_TABLE_NAME + "."+DBHelper.TABLE_ID + ", " +
                DBHelper.ITEM_VALUE + ", " +
                DBHelper.ITEM_DATE + ", " +
                DBHelper.ITEM_CATEGORY_ID + ", " +
                DBHelper.CATEGORY_TYPE + " FROM " +
                DBHelper.ITEM_TABLE_NAME + ", " +
                DBHelper.CATEGORY_TABLE_NAME + " where "+
                DBHelper.ITEM_TABLE_NAME + "." + DBHelper.ITEM_CATEGORY_ID +
                " = " + DBHelper.CATEGORY_TABLE_NAME +"." +DBHelper.TABLE_ID +
                " AND "+DBHelper.ITEM_DATE + " BETWEEN '" + from + "' AND '" + to+
                "' AND "+DBHelper.ITEM_TYPE+" = 1; ";

        db = dbHelp.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
