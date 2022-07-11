package com.mordansoft.angleofknife.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mordansoft.angleofknife.DatabaseHelper;
import com.mordansoft.angleofknife.R;
import com.mordansoft.angleofknife.models.Knife;

public class AngleActivity extends AppCompatActivity {
    private TextView tvAngleValue;
    private TextView tvAngleValueLevel;
    private TextView tvAngleKnifeName;
    private TextView tvAngleKnifeAngle;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private int levelDegree = 0;
    private int displayDegree = 0;
    private int sensorDegree = 0;
    String line = "__________________";

    private Knife knife;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, sensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener, sensor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle);
        Bundle arguments = getIntent().getExtras();
        long knifeId =arguments.getLong("EXTRA_KNIFE_ID");
        knife = Knife.getKnifeById(this,knifeId);
        tvAngleValue = findViewById(R.id.tv_angle_value);
        tvAngleValueLevel = findViewById(R.id.tv_angle_value_level);
        //tvAngleValueLevel.setVisibility(View.INVISIBLE);
        tvAngleKnifeName = findViewById(R.id.tv_angle_knife_name);
        tvAngleKnifeAngle = findViewById(R.id.tv_angle_knife_angle);
        tvAngleKnifeName.setText(knife.getName());
        tvAngleKnifeAngle.setText(String.valueOf(knife.getAngle()));
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float [16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                                                    SensorManager.AXIS_X,
                                                    SensorManager.AXIS_Z,
                                                    remappedRotationMatrix);
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i  = 0; i < 3; i++){
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }


                sensorDegree = (int) - orientations[2] - 90;
                displayDegree = Math.abs((sensorDegree - levelDegree)% 90);
                tvAngleValue.setRotation(sensorDegree);

                tvAngleValue.setText(line + String.valueOf(displayDegree) + line);
                if (displayDegree == knife.getAngle()){
                    tvAngleValue.setTextColor(Color.RED);
                } else {
                    tvAngleValue.setTextColor(Color.BLACK);
                }
                //ivAngleLevel.setRotation(-orientations[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    public void setLevel (View view){
        levelDegree = sensorDegree;
        if (sensorDegree != 0){
            //tvAngleValueLevel.setVisibility(View.GONE);
            tvAngleValueLevel.setText(line + line + line);
            tvAngleValueLevel.setTextColor(Color.GRAY);
            tvAngleValueLevel.setRotation(sensorDegree);
            Toast toast = Toast.makeText(getApplicationContext(), "level = "+ String.valueOf(sensorDegree),Toast.LENGTH_SHORT);
            toast.show();
        } else {
            //tvAngleValueLevel.setVisibility(View.INVISIBLE);
        }



    }


    public void saveKnife(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        knife.setLastSharpening(System.currentTimeMillis() / 1000L);
        Knife.updKnife(db,knife);
        db.close();
        Intent intent = new Intent(this,KnifeActivity.class);
        intent.putExtra("EXTRA_KNIFE_ID", knife.getId());
        startActivity(intent);
    }
}