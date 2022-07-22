package com.mordansoft.angleofknife.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.mordansoft.angleofknife.R;
import com.mordansoft.angleofknife.models.Knife;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class KnifeActivity extends AppCompatActivity {
    private Knife    knife;
    private EditText etKnifeName;
    private EditText etKnifeAngle;
    private EditText etKnifeDate;
    private EditText etKnifeDescription;
    private View btnKnifeSharpen, btnKnifeDelete;
    SimpleDateFormat dateFormat;

    java.util.Date normalTime;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knife);
        Bundle arguments = getIntent().getExtras();
        long knifeId =arguments.getLong(Knife.EXTRA_ID);

        if ( knifeId !=0 ){
            knife = Knife.getKnifeById(this, knifeId);
        } else {
            btnKnifeSharpen = findViewById(R.id.btn_knive_sharpen);
            btnKnifeDelete = findViewById(R.id.btn_knive_delete);
            btnKnifeSharpen.setVisibility(View.GONE);
            btnKnifeDelete.setVisibility(View.GONE);
            knife = Knife.getNewKnife(this);
        }

        buttonsNeighborAvailable();

        etKnifeName         = findViewById(R.id.et_knife_name);
        etKnifeAngle        = findViewById(R.id.et_knife_angle);
        etKnifeDate         = findViewById(R.id.et_knife_date);
        etKnifeDescription  = findViewById(R.id.et_knife_description);

        etKnifeName.setText(knife.getName());
        DecimalFormat dF = new DecimalFormat( "#" );
        etKnifeAngle.setText(dF.format(knife.getAngle()));
        etKnifeDate = findViewById(R.id.et_knife_date);
        normalTime=new java.util.Date(knife.getLastSharpening() * 1000);
        dateFormat = new SimpleDateFormat(this.getString(R.string.activity_knife_date_format)); //todo
        etKnifeDate.setText(dateFormat.format( normalTime ) );
        etKnifeDescription.setText(knife.getDescription());
    }


    /********** Buttons block **********/

    public void previousKnife(View view) {
        changeKnife(-1);
    }

    public void nextKnife(View view) {
        changeKnife(+1);
    }

    private void changeKnife(int position){
        if(Knife.neighborIsAvailable(this,knife.getId(),position)){
            Intent intent = new Intent(KnifeActivity.this, KnifeActivity.class);
            intent.putExtra(Knife.EXTRA_ID, knife.getId()+position);
            startActivity(intent);
        }
    }

    public void addEditKnife(View view) {
        long unixTime;
        try{    //validate the date of last sharpening
            Date date= dateFormat.parse(String.valueOf(etKnifeDate.getText())); //todo create new func
            unixTime = date.getTime()/1000;
        }catch(ParseException e){
            etKnifeDate.setText(dateFormat.format( normalTime ) );
            Toast toast = Toast.makeText(view.getContext(),
                    this.getString(R.string.activity_knife_error_wrong_date),
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        try{    //validate the angle of knife
            int angle = Integer.parseInt(String.valueOf(etKnifeAngle.getText()));
            if (angle > 90 || angle < 1){
                throw new Exception(this.getString(R.string.activity_knife_error_wrong_angle));
            }
        }catch(ParseException e){
            Toast toast = Toast.makeText(view.getContext(),
                    this.getString(R.string.activity_knife_error_wrong_angle),
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        } catch (Exception e) {
            Toast toast = Toast.makeText(view.getContext(), String.valueOf(e), Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        knife.setName(String.valueOf(etKnifeName.getText()));
        knife.setAngle(Integer.parseInt(String.valueOf(etKnifeAngle.getText())));
        knife.setDescription(String.valueOf(etKnifeDescription.getText()));
        knife.setLastSharpening(unixTime);

        if (knife.getId() != 0) {
            Knife.updKnife(view.getContext(), knife);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            long newKnifeId = Knife.insKnife(view.getContext(), knife);
            Intent intent = new Intent(this, KnifeActivity.class);
            intent.putExtra(Knife.EXTRA_ID, newKnifeId);
            startActivity(intent);
        }
    }

    public void sharpenKnife(View view) {
        Intent intent = new Intent(KnifeActivity.this, AngleActivity.class);
        intent.putExtra(Knife.EXTRA_ID, knife.getId());
        startActivity(intent);
    }

    public void deleteKnife(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.activity_knife_delete));
        builder.setMessage(getString(R.string.activity_knife_delete_message));
        builder.setPositiveButton(getString(R.string.activity_knife_yes), (dialog, id) -> {
            try {
                Knife.delKnife(view.getContext(), knife);
                Toast toast = Toast.makeText(view.getContext(),
                        this.getString(R.string.activity_knife_delete_complete_message),
                        Toast.LENGTH_LONG);
                Intent intent;
                intent = new Intent(view.getContext(), MainActivity.class);
                toast.show();
                startActivity(intent);
            } catch (Exception e){
                Toast toast = Toast.makeText(view.getContext(),
                        "deleteKnife error: " + e,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        builder.setNegativeButton(this.getString(R.string.activity_knife_no), (dialog, id) -> dialog.cancel());
        builder.show();
    }

    public void buttonsNeighborAvailable(){
        View previousKnife = findViewById(R.id.cl_knife_previous);
        if(Knife.neighborIsAvailable(this,knife.getId(),-1)){
            previousKnife.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow));
        } else {
            previousKnife.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow_off));
        }
        View nextKnife = findViewById(R.id.cl_knife_next);

        if(Knife.neighborIsAvailable(this,knife.getId(),+1)){
            nextKnife.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow));
        } else {
            nextKnife.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_arrow_off));
        }
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