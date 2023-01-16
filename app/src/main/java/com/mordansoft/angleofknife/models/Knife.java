package com.mordansoft.angleofknife.models;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.mordansoft.angleofknife.DatabaseHelper;
import com.mordansoft.angleofknife.R;

import java.util.ArrayList;
import java.util.Calendar;


public class Knife {
    private final long id;
    private String name;
    private String description;
    private float angle;
    private long lastSharpening;
    private int status;
    public static final String EXTRA_ID  = "EXTRA_KNIFE_ID";
    private boolean doubleSideSharp;

    /****** Constructors getters setters *******/ //todo refactor
    public Knife(long id, String name, String description, float angle, long lastSharpening, int status, boolean doubleSideSharp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.angle = angle;
        this.lastSharpening = lastSharpening;
        this.status = status;
        this.doubleSideSharp = doubleSideSharp;
    }
    public long getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getAngle() {
        return angle;
    }
    public void setAngle(float angle) {
        this.angle = angle;
    }
    public long getLastSharpening() {
        return lastSharpening;
    }
    public void setLastSharpening(long lastSharpening) {
        this.lastSharpening = lastSharpening;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public boolean isDoubleSideSharp() {return doubleSideSharp;}
    public void setDoubleSideSharp(boolean doubleSideSharp) {this.doubleSideSharp = doubleSideSharp;}
    /***** !Constructors getters setters *******/


    public static Knife getKnifeById(Context context, long id){
        SQLiteDatabase db;
        Knife knife = null;
        try {
            SQLiteOpenHelper databaseHelper;

            databaseHelper = new DatabaseHelper(context);
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TBL_KNIVES, new String[]{"name", //0
                                                                "description",//1
                                                                "angle",//2
                                                                "last_sharpening",//3
                                                                "status",//4
                                                                "double_side_sharpening"},//5
                    "_id = ?", new String[] {String.valueOf(id)},null,null,null);

            if (cursor.moveToFirst()){
                knife = new Knife(
                        id,
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getLong(3),
                        cursor.getInt(4),
                        intToBool(cursor.getInt(5)));
            }
            cursor.close();
            db.close();
        } catch(Exception e) {
            Toast toast = Toast.makeText(context, "getKnifeById error: " + e, Toast.LENGTH_SHORT);
            toast.show();
        }
        return knife;
    }

    public  static Knife getNewKnife(Context context){
        String name =  context.getString(R.string.stock_data_knife_name);
        String description = context.getString(R.string.stock_data_knife_name);
        int angle = 35;
        long date = System.currentTimeMillis() / 1000L;
        int status = Status.STATUS_NEW;
        boolean doubleSideSharp = true;
        return new Knife(0, name, description, angle, date, status, doubleSideSharp);
    }

    public static ArrayList<Knife> getActiveKnivesFromDatabase(Context context){
        ArrayList<Knife> listKnives= new ArrayList<>();
        try {

            String query = "status < " + Status.STATUS_DISABLE;
            SQLiteOpenHelper databaseHelper;
            SQLiteDatabase db;
            databaseHelper = new DatabaseHelper(context);
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TBL_KNIVES, new String[]{"_id",
                            "name",                     //1
                            "description",              //2
                            "angle",                    //3
                            "last_sharpening",          //4
                            "status",                   //5
                            "double_side_sharpening"},     //6
                    query, null,null,null,"_id");

            while (cursor.moveToNext()){
                long    id              = cursor.getLong(0);
                String  name            = cursor.getString(1);
                String  description     = cursor.getString(2) ;
                float   angle           = cursor.getFloat(3);
                long    lastSharpening  = cursor.getLong(4);
                int     status          = cursor.getInt(5);
                boolean doubleSideSharp    = intToBool(cursor.getInt(6));
                listKnives.add(new Knife(id,name, description, angle, lastSharpening, status, doubleSideSharp));
            }
            cursor.close();
            db.close();
        } catch(Exception e) {
            Toast toast = Toast.makeText(context, "getKniVesFromDatabase error: " + e, Toast.LENGTH_LONG);
            toast.show();
        }
        return listKnives;
    }

    public static long insKnife(Context context, Knife knife){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        knife.status = Status.STATUS_NEW;
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", knife.name);
        contentValues.put("description", knife.description);
        contentValues.put("angle", knife.angle);
        contentValues.put("last_sharpening", knife.lastSharpening);
        contentValues.put("status", knife.status);
        contentValues.put("double_side_sharpening", knife.isDoubleSideSharp());
        long result = db.insert(DatabaseHelper.TBL_KNIVES,null,contentValues);
        db.close();
        return (result);
    }

    public static void updKnife(Context context, Knife knife){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", knife.getName());
        contentValues.put("description", knife.getDescription());
        contentValues.put("angle", knife.getAngle());
        contentValues.put("last_sharpening", knife.getLastSharpening());
        contentValues.put("status", knife.getStatus());
        contentValues.put("double_side_sharpening", knife.isDoubleSideSharp());

        db.update(DatabaseHelper.TBL_KNIVES,contentValues, "_id = ?",new String[] {String.valueOf(knife.id)});
        db.close();
    }

    public static void sharpKnife(Context context, Knife knife){ //todo create new toasts in funcs
        knife.lastSharpening = System.currentTimeMillis()/1000;
        updKnife(context,knife);
    }

    public static void delKnife(Context context, Knife knife){
       knife.status = Status.STATUS_DISABLE;
       updKnife(context,knife);
    }

    public static void insertConstantsData(Context context) {
        Calendar calendar = Calendar.getInstance();

        long sharpeningTime = calendar.getTimeInMillis();

        insKnife(context, new Knife(0, context.getString(R.string.demo_data_pocket_knife), context.getString(R.string.demo_data_pocket_knife), 35, sharpeningTime, Status.STATUS_NEW, true));
        insKnife(context, new Knife(0, context.getString(R.string.demo_data_chef_knife),context.getString(R.string.demo_data_pocket_knife), 30, sharpeningTime, Status.STATUS_NEW, true));
        //insKnife(context, new Knife(0, context.getString(R.string.demo_data_meat_knife), context.getString(R.string.demo_data_pocket_knife), 25, sharpeningTime, Status.STATUS_NEW, true));
        insKnife(context, new Knife(0, context.getString(R.string.demo_data_fish_knife), context.getString(R.string.demo_data_pocket_knife), 20, sharpeningTime, Status.STATUS_NEW, true));
        //insKnife(context, new Knife(0, context.getString(R.string.demo_data_fruit_knife), context.getString(R.string.demo_data_pocket_knife), 15, sharpeningTime, Status.STATUS_NEW, true));
        insKnife(context, new Knife(0, context.getString(R.string.demo_data_utility_knife), context.getString(R.string.demo_data_pocket_knife), 40, sharpeningTime, Status.STATUS_NEW, true));
        insKnife(context, new Knife(0, context.getString(R.string.demo_data_scissors), context.getString(R.string.demo_data_pocket_knife), 70, sharpeningTime, Status.STATUS_NEW, false));
    }

    public static boolean neighborIsAvailable(Context context, long knifeId, int position){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        boolean available = false;
        try {
            Cursor cursor = db.query(DatabaseHelper.TBL_KNIVES, new String[]{
                            "name"},
                    "_id = ?", new String[]{Long.toString(knifeId + position)},null,null,null);

            if (cursor.moveToFirst()){
                available = true;
            }
            cursor.close();
        } catch(Exception e) {
            Toast toast = Toast.makeText(context, "neighborIsAvailable error: " + e, Toast.LENGTH_LONG);
            toast.show();
        }
        db.close();
        return available;
    }

    public static boolean intToBool(int value){
        boolean result = false;
        if (value>0){
            result = true;
        }
        return result;
    }


}
