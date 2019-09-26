package com.handytrip.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "HANDY_NOTIFICATION.db";
    private static final int DB_VER = 2;
    SQLiteDatabase db;

    private static NotificationDB instance;
    public static synchronized NotificationDB getInstance(Context context){
        if( instance == null){
            instance = new NotificationDB(context.getApplicationContext());
        }
        return instance;
    }

    public NotificationDB(Context context) {
        super(context, DB_NAME, null, DB_VER);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NOTIFICATION (" +
                "DATE TEXT," +
                "NOTIFICATION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NOTIFICATION");
        onCreate(sqLiteDatabase);
    }

    public void insertNotification(String notification){
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. a HH:mm", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        values.put("DATE", sdf.format(calendar.getTime()));
        values.put("NOTIFICATION", notification);
        db.insert("NOTIFICATION", null, values);
    }

    public Cursor selectNotification(){
        return db.query("NOTIFICATION", null, null, null, null, null, "DATE DESC");
    }

    public void deleteNotification(String date, String text){
        db.delete("NOTIFICATION", "DATE=? AND NOTIFICATION=?", new String[]{date, text});
    }
}
