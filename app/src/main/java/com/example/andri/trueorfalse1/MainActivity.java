package com.example.andri.trueorfalse1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mediaPlayer;
    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus;
    ImageView soundIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        loadSoundText();

        soundIV = (ImageView) findViewById(R.id.mainSettingsIV);
        soundIV.setOnClickListener(this);
        if(soundStatus.equals("on")){
            soundIV.setImageResource(R.drawable.sound_on1);
        }else{
            soundIV.setImageResource(R.drawable.sound_off1);
        }

        Button exitBtn = (Button) findViewById(R.id.exitBtn);
        Button rulesBtn = (Button) findViewById(R.id.rulesBtn);
        rulesBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        TextView trueTV = (TextView) findViewById(R.id.trueTV);
        TextView falseTV = (TextView) findViewById(R.id.falseTV);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");
        trueTV.setTypeface(custom_font, Typeface.BOLD);
        falseTV.setTypeface(custom_font, Typeface.BOLD);

        Button playBtn = (Button) findViewById(R.id.playBtn);
        Button scoreBtn = (Button) findViewById(R.id.scoreBtn);
        playBtn.setOnClickListener(this);
        scoreBtn.setOnClickListener(this);

        mediaPlayer= MediaPlayer.create(this, R.raw.false_answer);


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
        Intent intent;
        switch (v.getId()) {
            case R.id.playBtn:
//                mediaPlayer.start();
//                mediaPlayer.stop();
                intent = new Intent(this, ModsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.rulesBtn:
                intent = new Intent(this, RulesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.exitBtn:
                finishAffinity();
                break;
            case R.id.scoreBtn:
                intent = new Intent("android.intent.action.activitytwo");
                startActivity(intent);
                break;
            case R.id.mainSettingsIV:
                if(soundStatus.equals("on")){
                    soundStatus = "off";
                    soundIV.setImageResource(R.drawable.sound_off1);
                    saveSoundText();
                }else{
                    soundStatus = "on";
                    soundIV.setImageResource(R.drawable.sound_on1);
                    saveSoundText();
                }
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadSoundText();
        if(soundStatus.equals("on")){
            soundIV.setImageResource(R.drawable.sound_on1);
        }else{
            soundIV.setImageResource(R.drawable.sound_off1);
        }
    }

}
