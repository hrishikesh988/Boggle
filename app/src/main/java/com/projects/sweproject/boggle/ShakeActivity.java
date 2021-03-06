package com.projects.sweproject.boggle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by emenpy on 2/26/17.
 */

public class ShakeActivity extends Activity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Button shake_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_screen);

        Bundle extras = getIntent().getExtras();
        final String Mode = extras.getString("MODE");
        final String Level = extras.getString("LEVEL");
        final String PlayerType = extras.getString("TYPE");

        //Toast.makeText(getApplicationContext(), "TYPE: "+ PlayerType, Toast.LENGTH_LONG).show();

        shake_button = (Button) findViewById(R.id.button5);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */

            /*
                finish();
                startActivity(getIntent());
                newIntent(Context packageContext, String gameLevel, String playerType)
        */
                if(Mode.equals("SinglePlayer")) {
                    System.out.println("SHAKE count " + count);
                    Intent in = spNewGame.newIntent(ShakeActivity.this, Level);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                else if(Mode.equals("BASIC")) {
                    System.out.println("SHAKE count " + count);
                    Intent in = mpNewGame.newIntent(ShakeActivity.this, Level, PlayerType, "BASIC");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }
                else if(Mode.equals("CUTTHROAT")) {
                    System.out.println("SHAKE count " + count);
                    Intent in = mpNewGame.newIntent(ShakeActivity.this, Level, PlayerType, "CUTTHROAT");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }
                else if(Mode.equals("ROUNDS")) {
                    System.out.println("SHAKE count " + count);
                    Intent in = mpNewGame.newIntent(ShakeActivity.this, Level, PlayerType,"ROUNDS");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }



            }



        });

        shake_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Mode.equals("SinglePlayer")) {
                    //System.out.println("SHAKE count " + count);
                    Intent in = spNewGame.newIntent(ShakeActivity.this, Level);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

                else if(Mode.equals("BASIC")) {
                   // System.out.println("SHAKE count " + count);
                    Intent in = mpNewGame.newIntent(ShakeActivity.this, Level, PlayerType, "BASIC");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }
                else if(Mode.equals("CUTTHROAT")) {
                    //System.out.println("SHAKE count " + count);
                    Intent in = mpNewGame.newIntent(ShakeActivity.this, Level, PlayerType, "CUTTHROAT");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }
                else if(Mode.equals("ROUNDS")) {
                    //System.out.println("SHAKE count " + count);
                    Intent in = mpNewGame.newIntent(ShakeActivity.this, Level, PlayerType, "ROUNDS");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                }

            }
        });



    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    public static Intent newIntent(Context packageContext, String PlayerType, String gameMode, String gameLevel) {
        Intent i = new Intent( packageContext, ShakeActivity.class);
        i.putExtra("TYPE",PlayerType);
        i.putExtra("MODE",gameMode);
        i.putExtra("LEVEL",gameLevel);


        return i;
    }

}
