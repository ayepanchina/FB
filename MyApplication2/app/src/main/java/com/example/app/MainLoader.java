package com.example.app;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

/**
 * Created by Вадим on 19.05.14.
 */
public class MainLoader extends CursorLoader {
    DatabaseProvider provider;

    public static MainLoader getInstance() {
        return instance;
    }

    static private MainLoader instance;

    public MainLoader(Context context, DatabaseProvider provider, int action) {
        super(context);
        this.provider = provider;
        if (action == 0) {
            this.instance = this;
        }
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor;
        cursor = provider.getExpenseCategories();
        return cursor;
    }


}
