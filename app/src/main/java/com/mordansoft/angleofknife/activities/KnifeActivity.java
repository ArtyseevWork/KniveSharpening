package com.mordansoft.angleofknife.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mordansoft.angleofknife.DatabaseHelper;
import com.mordansoft.angleofknife.R;
import com.mordansoft.angleofknife.models.Knife;
import com.mordansoft.angleofknife.models.Status;

import java.text.SimpleDateFormat;


public class KnifeActivity extends AppCompatActivity {
    private Knife    knife;
    private EditText etKnifeName;
    private EditText etKnifeAngle;
    private EditText etKnifeDate;
    private EditText etKnifeDescription;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    java.util.Date normalTime;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knife);
        Bundle arguments = getIntent().getExtras();
        long knifeId =arguments.getLong("EXTRA_KNIFE_ID");



        if ( knifeId !=0 ){
            knife = Knife.getKnifeById(this, knifeId);
        } else {
            knife = new Knife(0,"Name","Description", 35, System.currentTimeMillis() / 1000L, Status.statusNew);
        }

        etKnifeName         = findViewById(R.id.et_knife_name);
        etKnifeAngle        = findViewById(R.id.et_knife_angle);
        etKnifeDate         = findViewById(R.id.et_knife_date);
        etKnifeDescription  = findViewById(R.id.et_knife_description);

        etKnifeName.setText(knife.getName());
        etKnifeAngle.setText(String.valueOf(knife.getAngle()));
        etKnifeDate = findViewById(R.id.et_knife_date);
        normalTime=new java.util.Date(knife.getLastSharpening() * 1000);
        etKnifeDate.setText(dateFormat.format( normalTime ) );
        etKnifeDescription.setText(knife.getDescription());
    }


    /********** Buttons block **********/
    public void nextKnife(View view) {
            Intent intent = new Intent(KnifeActivity.this, KnifeActivity.class);
            intent.putExtra("EXTRA_KNIFE_ID", knife.getId()+1);
            startActivity(intent);
    }

    public void previousKnife(View view) {
        Intent intent = new Intent(KnifeActivity.this, KnifeActivity.class);
        intent.putExtra("EXTRA_KNIFE_ID", knife.getId()-1);
        startActivity(intent);
    }

    public void addEditKnife(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Knife.updKnife(db,knife);
        db.close();
    }

    public void sharpenKnife(View view) {
        Intent intent = new Intent(KnifeActivity.this, AngleActivity.class);
        intent.putExtra("EXTRA_KNIFE_ID", knife.getId());
        startActivity(intent);

    }

    public void deleteKnife(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        knife.setStatus(Status.statusDis);
        Knife.updKnife(db,knife);
        db.close();
    }
    /* ******* ! Buttons block **********/


    /********** Button back **********/
    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
    /* *******! Button back **********/

}