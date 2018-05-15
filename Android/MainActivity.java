package com.prchandr.segway;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Sensor orientationSensor;
    SensorManager sensorManager;
    SensorEventListener orientationListener;

    Thread thread;
    Socket socket;
    DataOutputStream outputStream;

    Button connect;
    Button disconnect;
    TextView dataView;
    TextView statusView;

    String rotation;
    String dataSent;

    boolean changed;
    float[] orientationData;
    boolean sendCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dataView = (TextView) findViewById(R.id.text);
        statusView = (TextView) findViewById(R.id.status);
        sendCheck = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        orientationData = new float[]{0, 0, 0};
        dataSent = "";

        connect = (Button) findViewById(R.id.connect);
        disconnect = (Button) findViewById(R.id.disconnect);


        connect.setBackgroundColor(Color.GREEN);
        disconnect.setBackgroundColor(Color.GREEN);

        statusView.setText("Disconnected");
        changed = false;

        orientationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                orientationData = sensorEvent.values.clone();
                viewOrientation();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCheck = true;
                if(thread == null) {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                socket = new Socket("10.0.0.1", 100);
                                outputStream = new DataOutputStream(socket.getOutputStream());
                                while (sendCheck) {
                                    if (changed) {
                                        byte[] dataSentBytes = dataSent.getBytes();
                                        outputStream.write(dataSentBytes);
                                        outputStream.flush();
                                        changed = false;
                                    }
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                outputStream.close();
                                socket.close();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    thread.start();
                    statusView.setText("Sending Data...");
                    connect.setBackgroundColor(Color.RED);
                    disconnect.setBackgroundColor(Color.RED);
                }
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendCheck) {
                    endThread();
                    statusView.setText("Disconnected");
                    connect.setBackgroundColor(Color.GREEN);
                    disconnect.setBackgroundColor(Color.GREEN);
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(orientationListener, orientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        endThread();
        sensorManager.unregisterListener(orientationListener);
    }

    public void viewOrientation(){
        rotation = "";

        float azimuth = orientationData[0];
        float pitch = orientationData[1];
        float roll = orientationData[2];

        dataSent = ","+azimuth + "," + pitch + "," + roll;

        rotation = azimuth + "\n" + pitch + "\n" + roll ;
        dataView.setText(rotation);
        changed = true;
    }

    public void endThread(){
        sendCheck = false;
        if(thread != null){
            thread.interrupt();
            thread = null;
        }
    }
}
