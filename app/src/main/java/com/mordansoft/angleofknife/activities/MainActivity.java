package com.mordansoft.angleofknife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.mordansoft.angleofknife.models.Knife;
import com.mordansoft.angleofknife.KnifeAdapter;
import com.mordansoft.angleofknife.R;

import java.util.prefs.Preferences;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static long back_pressed;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        updateUi();
    }

    private void init(){
        setContentView(R.layout.activity_main);
        com.mordansoft.angleofknife.models.Preferences.firstRunWizard(this);
        createRecyclerView(R.id.rv_main);
    }

    private void updateUi(){
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView menuButton = findViewById(R.id.iv_main_btn_menu);
        ImageView addKnifeButton = findViewById(R.id.iv_main_btn_add);
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

    public void addKnife(View view) {
        Intent intent = new Intent(MainActivity.this, KnifeActivity.class);
        intent.putExtra(Knife.EXTRA_ID, 0);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) { //buttons of left menu
        int id = item.getItemId();
        Intent intent;
        switch(id){ //todo create other items of menu (handbook...)
            case R.id.menuFeedback:
                intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    /******* listeners *********/
    View.OnClickListener addKnife = v -> {
        Intent intent = new Intent(MainActivity.this, KnifeActivity.class);
        intent.putExtra(Knife.EXTRA_ID, 0);
        startActivity(intent);
    };

    View.OnClickListener openMenu = v -> {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    };

    /***** ! listeners *********/
    @Override
    public void onBackPressed() {   //exit from app
        DrawerLayout drawer = findViewById(R.id.drawer_layout); //hide left menu
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
}