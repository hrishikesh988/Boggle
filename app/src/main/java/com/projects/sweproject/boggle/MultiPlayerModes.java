package com.projects.sweproject.boggle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by minphan on 3/4/17.
 */

public class MultiPlayerModes extends Activity {



    ImageButton basic;
    ImageButton cutthroat;
    ImageButton multirounds;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.multi_player);


        basic = (ImageButton)findViewById(R.id.basicbtn);
        basic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent in = MultiPlayerLevels.newIntent(getApplicationContext(),"HOST", "BASIC");
                startActivity(in);


            }
        });

        cutthroat = (ImageButton)findViewById(R.id.cutthroatbtn);
        cutthroat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = MultiPlayerLevels.newIntent(getApplicationContext(),"HOST", "CUTTHROAT");
                startActivity(in);

            }
        });

        multirounds = (ImageButton)findViewById(R.id.multiroundsbttn);

        multirounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = MultiPlayerLevels.newIntent(getApplicationContext(),"HOST", "ROUNDS");
                startActivity(in);

            }
        });







    }

}
