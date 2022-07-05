package com.example.knifesharpening.models;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.knifesharpening.DatabaseHelper;

import java.util.ArrayList;


public class Knive {
    private long id;
    private String name;
    private String description;
    private float angle;
    private long lastSharpening;
    private int status;

    /****** Constructors getters setters *******/
    public Knive(long id, String name, String description, float angle, long lastSharpening, int status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.angle = angle;
        this.lastSharpening = lastSharpening;
        this.status = status;
    }
    public long getId() {
        return this.id;
    }
    public String getStrId() {

        return String.valueOf(this.id);
    }

    public void setId(int id) {
        this.id = id;
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
    /***** !Constructors getters setters *******/

    public static Knive getKniveById(Context context, long id){
        SQLiteDatabase db;
        Knive knive = null;
        try {
            SQLiteOpenHelper databaseHelper;

            databaseHelper = new DatabaseHelper(context);
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query("KNIVES", new String[]{"name", //0
                                                                "description",//1
                                                                "angle",//2
                                                                "last_sharpening",//3
                                                                "status"},//4
                    "_id = ?", new String[] {String.valueOf(id)},null,null,null);

            if (cursor.moveToFirst()){
                Toast toast = Toast.makeText(context, "knive " + cursor.getString(0), Toast.LENGTH_LONG);
                toast.show();
                knive = new Knive(
                        id,
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getLong(3),
                        cursor.getInt(4));
            }
            cursor.close();
            db.close();
        } catch(Exception e) {
            Toast toast = Toast.makeText(context, "getkniveById error: " + e, Toast.LENGTH_SHORT);
            toast.show();
        }

        return knive;
    }

    public static ArrayList<Knive> getActiveKnivesFromDatabase(Context context){
        ArrayList<Knive> listKnives= new ArrayList<>();
        try {

            String query = "status < " + Status.statusDis;
            SQLiteOpenHelper databaseHelper;
            SQLiteDatabase db;
            databaseHelper = new DatabaseHelper(context);
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query("KNIVES", new String[]{"_id",
                            "name",
                            "description",
                            "angle",
                            "last_sharpening",
                            "status"},
                    query, null,null,null,"last_sharpening");

            while (cursor.moveToNext()){
                long    id              = cursor.getLong(0);
                String  name            = cursor.getString(1);
                String  description     = cursor.getString(2) ;
                float   angle           = cursor.getFloat(3);
                long    lastSharpening  = cursor.getLong(4);
                int     status          = cursor.getInt(5);
                listKnives.add(new Knive(id,name, description, angle, lastSharpening, status));
            }
            cursor.close();
            db.close();
        } catch(Exception e) {
            Toast toast = Toast.makeText(context, "getknivesFromDatabase error: " + e, Toast.LENGTH_SHORT);
            toast.show();
        }
        return listKnives;
    }

    public static long insKnive(SQLiteDatabase db, Knive knive){
        knive.status = Status.statusNew;
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", knive.name);
        contentValues.put("description", knive.description);
        contentValues.put("angle", knive.angle);
        contentValues.put("last_sharpening", knive.lastSharpening);
        contentValues.put("status", knive.status);
        return ( db.insert("KNIVES",null,contentValues));
    }

    public static void updKnive(SQLiteDatabase db, Knive knive){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", knive.name);
        contentValues.put("description", knive.description);
        contentValues.put("angle", knive.angle);
        contentValues.put("last_sharpening", knive.lastSharpening);
        contentValues.put("status", knive.status);
        db.update("KNIVES",contentValues, "_id = ?",new String[] {String.valueOf(knive.id)});
    }

    public static void sharpKnive(SQLiteDatabase db, Knive knive){
        knive.lastSharpening = System.currentTimeMillis();;
        updKnive(db,knive);
    }
    public static void delKnive(SQLiteDatabase db, Knive knive){
       knive.status = Status.statusDis;
       updKnive(db,knive);
    }


}
