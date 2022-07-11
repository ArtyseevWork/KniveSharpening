package com.mordansoft.angleofknife;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mordansoft.angleofknife.models.Knife;


public class DatabaseHelper extends SQLiteOpenHelper {
// Context context;
    public static final String dbName = "db_knife";
    public static final int ver = 1;

    public DatabaseHelper(Context context) {
        super(context, dbName, null, ver);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db,0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVer){
        if (oldVer < 1) { // first run on this devise
            try {
                deleteAllTables(db);
                createAllTables(db);
                insertConstantsData(db);
            } catch (Exception e) {}
        }
    }

    public static void deleteAllTables(SQLiteDatabase db){
        try {
                db.execSQL("DROP TABLE IF EXISTS '" + "KNIFES" + "'");
        } catch (Exception e) {
        }
    }


    private static void createAllTables(SQLiteDatabase db){
        db.execSQL("CREATE TABLE KNIFES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT,"
                + "description TEXT,"
                + "angle NUMERIC,"
                + "status INTEGER,"
                + "last_sharpening NUMERIC);"
        );
    }

    public static void insertConstantsData(SQLiteDatabase db){
        Knife.insKnife(db, new Knife(0, "cheese knife1", "present from my granny2", 35, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife2", "present from my granny1", 25, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife3", "present from my granny3", 15, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife4", "present from my granny4", 45, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife5", "present from my granny5", 23, 0, 400));
        Knife.insKnife(db, new Knife(0, "cheese knife6", "present from my granny46", 45, 0, 400));
        Knife.insKnife(db, new Knife(0, "cheese knife41", "present from my granny2", 35, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife42", "present from my granny1", 25, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife43", "present from my granny3", 15, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife44", "present from my granny4", 45, 0, 100));
        Knife.insKnife(db, new Knife(0, "cheese knife45", "present from my granny5", 23, 0, 400));
        Knife.insKnife(db, new Knife(0, "cheese knife46", "present from my granny46", 45, 0, 400));

    }
}
