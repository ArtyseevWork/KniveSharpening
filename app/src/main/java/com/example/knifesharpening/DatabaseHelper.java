package com.example.knifesharpening;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.knifesharpening.models.Knive;


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
                db.execSQL("DROP TABLE IF EXISTS '" + "KNIVES" + "'");
        } catch (Exception e) {
        }
    }


    private static void createAllTables(SQLiteDatabase db){
        db.execSQL("CREATE TABLE KNIVES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT,"
                + "description TEXT,"
                + "angle NUMERIC,"
                + "status INTEGER,"
                + "last_sharpening NUMERIC);"
        );
    }

    public static void insertConstantsData(SQLiteDatabase db){
        Knive.insKnive(db, new Knive(0, "cheese knife1", "present from my granny2", 35, 0, 100));
        Knive.insKnive(db, new Knive(0, "cheese knife2", "present from my granny1", 25, 0, 100));
        Knive.insKnive(db, new Knive(0, "cheese knife3", "present from my granny3", 15, 0, 100));
        Knive.insKnive(db, new Knive(0, "cheese knife4", "present from my granny4", 45, 0, 100));
        Knive.insKnive(db, new Knive(0, "cheese knife5", "present from my granny5", 23, 0, 400));
        Knive.insKnive(db, new Knive(0, "cheese knife6", "present from my granny46", 45, 0, 400));

    }
}
