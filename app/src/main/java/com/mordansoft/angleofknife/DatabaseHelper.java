package com.mordansoft.angleofknife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String dbName = "db_angle_of_knife";
    public static final int ver = 2;
    public static final String TBL_KNIVES = "KNIVES";


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
                //insertConstantsData(db);
            } catch (Exception e) {
            }
        }
        if (oldVer == 1) { // update database
                db.execSQL("alter table KNIVES add column double_side_sharpening INTEGER DEFAULT 1");
        }
    }

    public static void deleteAllTables(SQLiteDatabase db){
        try {
                db.execSQL("DROP TABLE IF EXISTS '" + TBL_KNIVES + "'");
        } catch (Exception e) {

        }
    }

    private static void createAllTables(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TBL_KNIVES + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT,"
                + "description TEXT,"
                + "angle NUMERIC,"
                + "status INTEGER,"
                + "last_sharpening NUMERIC,"
                + "double_side_sharpening INTEGER)"
        );
    }

}
