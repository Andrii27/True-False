package com.example.andri.trueorfalse1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class RulesActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView rulesBackIV;
    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus = "on";
    ImageView soundIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rules);

        loadSoundText();

        soundIV = (ImageView) findViewById(R.id.rulesSettingsIV);
        soundIV.setOnClickListener(this);
        if(soundStatus.equals("on")){
            soundIV.setImageResource(R.drawable.sound_on1);
        }else{
            soundIV.setImageResource(R.drawable.sound_off1);
        }

        rulesBackIV = (ImageView) findViewById(R.id.rulesBack);
        rulesBackIV.setOnClickListener(this);
    }

    private void saveSoundText() {
        sPref = getSharedPreferences("mySound", MODE_APPEND);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT,soundStatus);
        ed.commit();
    }

    private void loadSoundText() {
        sPref = getSharedPreferences("mySound", MODE_APPEND);
        String savedtext = sPref.getString(SAVED_TEXT,"");
        soundStatus = savedtext;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rulesSettingsIV){
            if (soundStatus.equals("on")) {
                soundStatus = "off";
                soundIV.setImageResource(R.drawable.sound_off1);
                saveSoundText();
            } else {
                soundStatus = "on";
                soundIV.setImageResource(R.drawable.sound_on1);
                saveSoundText();
            }
        }else {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }
}
