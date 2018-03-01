package com.example.andri.trueorfalse1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus = "on";
    ImageView soundIV;

    private String[] selectedCategories;
    private ListView listView;
    private Button selectCatBtn;
    Intent intent;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category);

        loadSoundText();

        soundIV = (ImageView) findViewById(R.id.categorySettingsIV);
        soundIV.setClickable(true);
        soundIV.setOnClickListener(this);
        if(soundStatus.equals("on")){
            soundIV.setImageResource(R.drawable.sound_on1);
        }else{
            soundIV.setImageResource(R.drawable.sound_off1);
        }

        selectedCategories = getResources().getStringArray(R.array.categories);

        ImageView imageView = (ImageView) findViewById(R.id.categoryBack);
        imageView.setOnClickListener(this);
        selectCatBtn = (Button) findViewById(R.id.selectCategoryBtn);
        selectCatBtn.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.categoryLV);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        adapter = new ArrayAdapter<String>(this, R.layout.category_item, selectedCategories);

        listView.setAdapter(adapter);

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

    private ArrayList<String> catList = new ArrayList<String>();

    @Override
    public void onClick(View v) {
//        ArrayList<String> catList = new ArrayList<String>();
        if(v.getId() == R.id.selectCategoryBtn){
            getCategoryList();
        }
        if(v.getId() == R.id.categoryBack){
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
//            intent = new Intent(this, ModsActivity.class);
//            startActivity(intent);
        }
        if(v.getId() == R.id.categorySettingsIV){
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

    public void getCategoryList(){
        SparseBooleanArray sbArray = listView.getCheckedItemPositions();
        for (int i=0; i<sbArray.size(); i++){
            int key = sbArray.keyAt(i);
            if(sbArray.get(key)){
                catList.add(adapter.getItem(key));
//                    Log.d("myLogs", adapter.getItem(key));
            }
        }
        String[] args = new String[catList.size()];
        int i = 0;
        for (String str : catList) {
            args[i] = str;
//                Log.d("myLogs", str);
            i++;
        }
        catList.clear();
        intent = new Intent(this, CategoryModsActivity.class);
        intent.putExtra("catList", args);
        if(args.length > 0) {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }else
            Toast.makeText(this, "Оберіть категорії", Toast.LENGTH_SHORT).show();
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
