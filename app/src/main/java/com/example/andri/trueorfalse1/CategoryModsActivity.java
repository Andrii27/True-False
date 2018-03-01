package com.example.andri.trueorfalse1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class CategoryModsActivity extends AppCompatActivity implements View.OnClickListener {

    Button catSurvBtn, catTimeBtn, catMultiBtn;
    Intent intent;
    ImageView categoryModsBackIV;

    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus = "on";
    ImageView soundIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_mods);

        loadSoundText();

        soundIV = (ImageView) findViewById(R.id.categoryModsSettingsIV);
        soundIV.setClickable(true);
        soundIV.setOnClickListener(this);
        if(soundStatus.equals("on")){
            soundIV.setImageResource(R.drawable.sound_on1);
        }else{
            soundIV.setImageResource(R.drawable.sound_off1);
        }

        categoryModsBackIV = (ImageView) findViewById(R.id.categoryModsBack);
        categoryModsBackIV.setOnClickListener(this);
        catSurvBtn = (Button) findViewById(R.id.categorySurvivalBtn);
        catTimeBtn = (Button) findViewById(R.id.categoryTimeBtn);
        catMultiBtn = (Button) findViewById(R.id.categoryMultiBtn);
        catSurvBtn.setOnClickListener(this);
        catTimeBtn.setOnClickListener(this);
        catMultiBtn.setOnClickListener(this);

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
        Intent intent1;
        intent = getIntent();
        String[] str;
        switch (v.getId()) {
            case R.id.categorySurvivalBtn:
                intent1 = new Intent(this, SurvivalActivity.class);
                str = intent.getStringArrayExtra("catList");
                intent1.putExtra("catList", str);
                startActivity(intent1);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
            case R.id.categoryTimeBtn:
                intent1 = new Intent(this, TimeActivity.class);
                str = intent.getStringArrayExtra("catList");
                intent1.putExtra("catList", str);
                startActivity(intent1);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
            case R.id.categoryMultiBtn:
                intent1 = new Intent(this, MultiplayerActivity.class);
                str = intent.getStringArrayExtra("catList");
                intent1.putExtra("catList", str);
                startActivity(intent1);
                break;
            case R.id.categoryModsBack:
//                intent1 = new Intent(this, CategoryActivity.class);
//                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.categoryModsSettingsIV:
                if (soundStatus.equals("on")) {
                    soundStatus = "off";
                    soundIV.setImageResource(R.drawable.sound_off1);
                    saveSoundText();
                } else {
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
            soundIV.setImageResource(R.drawable.sound);
        }else{
            soundIV.setImageResource(R.drawable.sound_off);
        }
    }
}
