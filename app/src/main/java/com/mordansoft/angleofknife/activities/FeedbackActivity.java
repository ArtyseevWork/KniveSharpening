package com.mordansoft.angleofknife.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mordansoft.angleofknife.BuildConfig;
import com.mordansoft.angleofknife.R;

public class FeedbackActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button rateBtn = findViewById(R.id.btn_feedback_rate);
        rateBtn.setOnClickListener(btnRateListener);

        Button shareBtn = findViewById(R.id.btn_feedback_share);
        shareBtn.setOnClickListener(btnShareListener);
    }

    /******* Listeners *********/
    View.OnClickListener btnRateListener = v -> rateApp();

    View.OnClickListener btnShareListener = v -> {

        String shareMessage = getString(R.string.activity_feedback_recomend_text);
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";

        Intent intentInvite = new Intent(Intent.ACTION_SEND);
        intentInvite.setType("text/plain");
        String subject = getString(R.string.activity_feedback_recommend_subject);
        intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentInvite.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(intentInvite, "Share using"));
    };

    /* **** ! Listeners *********/

    private String getGooglePlayStoreUrl(){
        String id = this.getApplicationInfo().packageName; // current google play is   using package name as id

        PackageManager packageManager = this.getApplicationContext().getPackageManager();
        Uri marketUri = Uri.parse("market://details?id=" + id);
        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
        if (marketIntent.resolveActivity(packageManager) != null)
            return "market://details" + id;
        else
            return "https://play.google.com/store/apps/details" + id;
    }

    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        //flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;

        intent.addFlags(flags);
        return intent;
    }


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