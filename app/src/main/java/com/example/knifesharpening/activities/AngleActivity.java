package com.example.knifesharpening.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.knifesharpening.R;

public class AngleActivity extends AppCompatActivity {
    private TextView tvAngleValue;
    private View ivAngleLevel;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_angle);
        Bundle arguments = getIntent().getExtras();
        //Long kniveId =arguments.getLong("EXTRA_KNIVE_ID");
        tvAngleValue = findViewById(R.id.tv_angle_value);
        ivAngleLevel = findViewById(R.id.iv_angle_level);
        super.onCreate(savedInstanceState);
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
                tvAngleValue.setText(String.valueOf((int) orientations[2]));
                tvAngleValue.setRotation(-orientations[2]);
                //ivAngleLevel.setRotation(-orientations[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

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
}