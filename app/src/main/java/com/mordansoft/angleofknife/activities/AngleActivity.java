package com.mordansoft.angleofknife.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.mordansoft.angleofknife.R;
import com.mordansoft.angleofknife.models.Knife;
import android.media.SoundPool;



public class AngleActivity extends AppCompatActivity {
    private TextView tvAngleValue;
    private TextView tvAngleValueLevel;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private float levelDegree = 0;
    private double displayDegree = 0;
    private float sensorDegree = 0;
    String line;
    private Knife knife;
    private SoundPool soundPool;
    private int melodyId;
    private boolean playFlag = true;
    private boolean halfOfKnife = true;
    private float angleValue;
    private String angleValueTitle;


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
        line = this.getString(R.string.activity_angle_line);
        Bundle arguments = getIntent().getExtras();
        long knifeId =arguments.getLong(Knife.EXTRA_ID);
        knife = Knife.getKnifeById(this,knifeId);
        tvAngleValue = findViewById(R.id.tv_angle_value);
        tvAngleValueLevel = findViewById(R.id.tv_angle_value_level);
        //tvAngleValueLevel.setVisibility(View.INVISIBLE);
        TextView tvAngleKnifeName = findViewById(R.id.tv_angle_knife_name);
        TextView tvAngleKnifeAngle = findViewById(R.id.tv_angle_knife_angle);
        tvAngleKnifeName.setText(knife.getName());
        angleValue = knife.getAngle();
        angleValueTitle = String.valueOf(angleValue);
        halfOfKnife = knife.isDoubleSideSharp();
        if (halfOfKnife){
            angleValue = angleValue/2;
            angleValueTitle += " / 2 = " + angleValue;

        }
        tvAngleKnifeAngle.setText(angleValueTitle);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();
        melodyId = soundPool.load(this, R.raw.beep, 1);

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
                sensorDegree = - orientations[2] - 90;

                double scale = Math.pow(10, 1);
                displayDegree = Math.abs((sensorDegree - levelDegree)% 90);
                displayDegree = Math.ceil(displayDegree * scale) / scale;

                tvAngleValue.setRotation(sensorDegree);

                tvAngleValue.setText(line + displayDegree + line);
                if (displayDegree == angleValue){
                    tvAngleValue.setTextColor(Color.RED);
                    if(playFlag) {
                        float volume = 0.1f;
                        soundPool.play(melodyId, volume, volume, 1, 0, 1);
                        playFlag = false;
                    }
                } else {
                    playFlag = true;
                    tvAngleValue.setTextColor(Color.BLACK);
                }
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
            Toast toast = Toast.makeText(getApplicationContext(),
                    this.getString(R.string.activity_angle_horizontal_level,String.valueOf(levelDegree)),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void saveKnife(View view) {
        Knife.sharpKnife(view.getContext(), knife);
        Intent intent = new Intent(this,KnifeActivity.class);
        intent.putExtra(Knife.EXTRA_ID, knife.getId());
        startActivity(intent);
    }
}