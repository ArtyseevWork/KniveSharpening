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
        recyclerView.setHasFixedSize(true);
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
}