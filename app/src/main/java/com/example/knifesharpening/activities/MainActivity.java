package com.example.knifesharpening.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knifesharpening.DatabaseHelper;
import com.example.knifesharpening.models.Knive;
import com.example.knifesharpening.KniveAdapter;
import com.example.knifesharpening.R;


public class MainActivity extends AppCompatActivity {
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private static long back_pressed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRecyclerView(R.id.rv_main);

    }

    public void createRecyclerView(int view){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(false);
        RecyclerView recyclerView = findViewById(view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        KniveAdapter adapter = new KniveAdapter(Knive.getActiveKnivesFromDatabase(MainActivity.this), this);
        recyclerView.setAdapter(adapter);
        adapter.setListener(kniveId -> {
            Intent intent = new Intent(MainActivity.this, KniveActivity.class);
            intent.putExtra("EXTRA_KNIVE_ID", kniveId);
            startActivity(intent);
        });
    }

    public void addKnive(View view) {
        Intent intent = new Intent(MainActivity.this, KniveActivity.class);
        intent.putExtra("EXTRA_KNIVE_ID", 0);
        startActivity(intent);
    }

    /********** Exit from app **********/
    public void goBack() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.activity_main_exit_message),
                    Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
/******** ! Exit from app **********/

}