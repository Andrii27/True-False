package com.example.andri.trueorfalse1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ModsActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus = "on";
    ImageView soundIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mods);

        loadSoundText();

        soundIV = (ImageView) findViewById(R.id.modsSettingsIV);
        soundIV.setOnClickListener(this);
        if(soundStatus.equals("on")){
            soundIV.setImageResource(R.drawable.sound_on1);
        }else{
            soundIV.setImageResource(R.drawable.sound_off1);
        }

        TextView tx = (TextView)findViewById(R.id.trueModsTV);
        TextView tx2 = (TextView)findViewById(R.id.falseModsTV);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");
        tx.setTypeface(custom_font, Typeface.BOLD);
        tx2.setTypeface(custom_font, Typeface.BOLD);

        ImageView iv = (ImageView) findViewById(R.id.modsBack);
        iv.setOnClickListener(this);

        Button btnSurvival = (Button) findViewById(R.id.survivalBtn);
        btnSurvival.setOnClickListener(this);
        Button btnTime = (Button) findViewById(R.id.timeBtn);
        btnTime.setOnClickListener(this);
        Button btnMulti = (Button) findViewById(R.id.multiBtn);
        btnMulti.setOnClickListener(this);
        Button btnCategory = (Button) findViewById(R.id.categoryBtn);
        btnCategory.setOnClickListener(this);

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
        if (v.getId() == R.id.modsBack){
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
//            intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
        }
        if (v.getId() == R.id.survivalBtn){
            intent = new Intent(this, SurvivalActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }
        if(v.getId() == R.id.timeBtn){
            intent = new Intent(this, TimeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }
        if(v.getId() == R.id.multiBtn){
            intent = new Intent(this, MultiplayerActivity.class);
            startActivity(intent);
//            overridePendingTransition(R.anim.nothin, R.anim.alpha_out);
        }
        if(v.getId() == R.id.categoryBtn){
            intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
        if(v.getId() == R.id.modsSettingsIV) {
            if (soundStatus.equals("on")) {
                soundStatus = "off";
                soundIV.setImageResource(R.drawable.sound_off1);
                saveSoundText();
            } else {
                soundStatus = "on";
                soundIV.setImageResource(R.drawable.sound_on1);
                saveSoundText();
            }
        }
    }
}
