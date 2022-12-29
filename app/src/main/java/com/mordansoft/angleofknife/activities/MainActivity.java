package com.mordansoft.angleofknife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mordansoft.angleofknife.models.Knife;
import com.mordansoft.angleofknife.KnifeAdapter;
import com.mordansoft.angleofknife.R;


public class MainActivity extends AppCompatActivity {
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRecyclerView(R.id.rv_main);
        ImageView menuButton = (ImageView) findViewById(R.id.iv_main_btn_menu);
        ImageView addKnifeButton = (ImageView) findViewById(R.id.iv_main_btn_add);
        menuButton.setOnClickListener(openMenu);
        addKnifeButton.setOnClickListener(addKnife);

    }

    public void createRecyclerView(int view){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(false);
        RecyclerView recyclerView = findViewById(view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        KnifeAdapter adapter = new KnifeAdapter(Knife.getActiveKnivesFromDatabase(this), this);
        recyclerView.setAdapter(adapter);
        adapter.setListener(knifeId -> {
            Intent intent = new Intent(MainActivity.this, KnifeActivity.class);
            intent.putExtra(Knife.EXTRA_ID, knifeId);
            startActivity(intent);
        });
    }


    /******* listeners *********/

    View.OnClickListener addKnife = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, KnifeActivity.class);
            intent.putExtra(Knife.EXTRA_ID, 0);
            startActivity(intent);
        }
    };

    View.OnClickListener openMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }
    };

    /***** ! listeners *********/

    @Override
    public void onBackPressed() {   //exit from app
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
}