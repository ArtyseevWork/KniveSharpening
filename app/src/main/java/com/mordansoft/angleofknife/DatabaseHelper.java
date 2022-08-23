package com.mordansoft.angleofknife;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mordansoft.angleofknife.models.Knife;
import com.mordansoft.angleofknife.models.Status;


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
                insertConstantsData(db);
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

    public static void insertConstantsData(SQLiteDatabase db){
        insKnife(db, new Knife(0, "Knife 1",   "Demo record",  35, 1657553800,  Status.STATUS_NEW,true));
        insKnife(db, new Knife(0, "Knife 2",   "Demo record",  30, 1643320800,  Status.STATUS_NEW,true));
        insKnife(db, new Knife(0, "Knife 3",   "Demo record",  35, 1657080800,  Status.STATUS_NEW,true));
        insKnife(db, new Knife(0, "Knife 4",   "Demo record",  20, 1657080800,  Status.STATUS_NEW,false));

        /*
        insKnife(db, new Knife(0, "cheese knife1",  "present from my granny2",  35, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife2",  "present from my granny1",  25, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife3",  "present from my granny3",  15, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife4",  "present from my granny4",  45, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife5",  "present from my granny5",  23, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife6",  "present from my granny46", 45, 0,  Status.statusDis));
        insKnife(db, new Knife(0, "cheese knife41", "present from my granny2",  35, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife42", "present from my granny1",  25, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife43", "present from my granny3",  15, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife44", "present from my granny4",  45, 0,  Status.statusNew));
        insKnife(db, new Knife(0, "cheese knife45", "present from my granny5",  23, 0,  Status.statusDis));
        insKnife(db, new Knife(0, "cheese knife46", "present from my granny46", 45, 0,  Status.statusDis));
        */
    }
    private static long insKnife(SQLiteDatabase db, Knife knife){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", knife.getName());
        contentValues.put("description", knife.getDescription());
        contentValues.put("angle", knife.getAngle());
        contentValues.put("last_sharpening", knife.getLastSharpening());
        contentValues.put("status", knife.getStatus());
        contentValues.put("double_side_sharpening", knife.isDoubleSideSharp());

        return ( db.insert(TBL_KNIVES,null,contentValues));
    }

}
