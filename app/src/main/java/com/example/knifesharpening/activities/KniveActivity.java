package com.example.knifesharpening.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knifesharpening.DatabaseHelper;
import com.example.knifesharpening.R;
import com.example.knifesharpening.models.Knive;
import com.example.knifesharpening.models.Status;

import java.text.SimpleDateFormat;


public class KniveActivity extends AppCompatActivity {
    private Knive    knive;
    private EditText etKniveName;
    private EditText etKniveAngle;
    private EditText etKniveDate;
    private EditText etKniveDescription;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    java.util.Date normalTime;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knive);
        Bundle arguments = getIntent().getExtras();
        Long kniveId =arguments.getLong("EXTRA_KNIVE_ID");

        Toast toast = Toast.makeText(this, "knive 41 " + kniveId, Toast.LENGTH_LONG);
        toast.show();

        if ( kniveId !=0 ){
            knive = Knive.getKniveById(this, kniveId);
        } else {
            knive = new Knive(0,"Name","Description", 35, System.currentTimeMillis() / 1000L, Status.statusNew);
        }

        etKniveName         = findViewById(R.id.et_knive_name);
        etKniveAngle        = findViewById(R.id.et_knive_angle);
        etKniveDate         = findViewById(R.id.et_knive_date);
        etKniveDescription  = findViewById(R.id.et_knive_description);

        etKniveName.setText(knive.getName());
        etKniveAngle.setText(String.valueOf(knive.getAngle()));
        etKniveDate = findViewById(R.id.et_knive_date);
        normalTime=new java.util.Date(knive.getLastSharpening() * 1000);
        etKniveDate.setText(dateFormat.format( normalTime ) );
        etKniveDescription.setText(knive.getDescription());



    }
    public void nextKnive(View view) {
            Intent intent = new Intent(KniveActivity.this, KniveActivity.class);
            intent.putExtra("EXTRA_KNIVE_ID", knive.getId()+1);
            startActivity(intent);
    }

    public void previousKnive(View view) {
        Intent intent = new Intent(KniveActivity.this, KniveActivity.class);
        intent.putExtra("EXTRA_KNIVE_ID", knive.getId()-1);
        startActivity(intent);
    }

    public void addEditKnive(View view) {
    }

    public void sharpenKnive(View view) {
        Intent intent = new Intent(KniveActivity.this, AngleActivity.class);
        //intent.putExtra("EXTRA_KNIVE_ID", knive.getId());
        startActivity(intent);

    }

    public void deleteKnive(View view) {
    }
}