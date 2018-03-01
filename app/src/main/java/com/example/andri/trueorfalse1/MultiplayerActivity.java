package com.example.andri.trueorfalse1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class MultiplayerActivity extends AppCompatActivity implements View.OnClickListener{

    TextView factMultiTV1, factMultiTV2, scoreTableTV1, scoreTableTV2;
    Button trueMultiBtn1, trueMultiBtn2, falseMultiBtn1, falseMultiBtn2;
    ImageView rightMultiIV1, rightMultiIV2, wrongMultiIV1, wrongMultiIV2;
    RelativeLayout playerRL, playerRL2;

    DB db;
    Cursor cursor;

    private int id;
    Random random = new Random();
    String[] catArgs;

    boolean catFac = false;
    private String fact = "";
    private int answer = 0;
    private int score1 = 0;
    private int score2 = 0;

    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_multiplayer);

        anim = AnimationUtils.loadAnimation(this, R.anim.multi_anim);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.multi_anim2);
        playerRL = (RelativeLayout) findViewById(R.id.playerRL);
        playerRL2 = (RelativeLayout) findViewById(R.id.playerRL2);
        playerRL.setAnimation(anim);
//        mHandler1.postDelayed(mRunnable1, 300);
        playerRL2.setAnimation(anim2);

        factMultiTV1 = (TextView) findViewById(R.id.factMultiTV1);
        factMultiTV2 = (TextView) findViewById(R.id.factMultiTV2);
        scoreTableTV1 = (TextView) findViewById(R.id.scoreTableMultiTV1);
        scoreTableTV2 = (TextView) findViewById(R.id.scoreTableMultiTV2);
        rightMultiIV1 = (ImageView) findViewById(R.id.rightMultiIV1);
        rightMultiIV2 = (ImageView) findViewById(R.id.rightMultiIV2);
        wrongMultiIV1 = (ImageView) findViewById(R.id.wrongMultiIV1);
        wrongMultiIV2 = (ImageView) findViewById(R.id.wrongMultiIV2);
        trueMultiBtn1 = (Button) findViewById(R.id.trueMultiBtn1);
        trueMultiBtn2 = (Button) findViewById(R.id.trueMultiBtn2);
        falseMultiBtn1 = (Button) findViewById(R.id.falseMultiBtn1);
        falseMultiBtn2 = (Button) findViewById(R.id.falseMultiBtn2);
        trueMultiBtn1.setOnClickListener(this);
        trueMultiBtn2.setOnClickListener(this);
        falseMultiBtn1.setOnClickListener(this);
        falseMultiBtn2.setOnClickListener(this);

        db = new DB(this);
        db.open();

        catFac = false;
        score1 = 0;
        score2 = 0;
        Intent intent = getIntent();
        if (intent.getStringArrayExtra("catList") != null) {
            catFac = true;
            catArgs = intent.getStringArrayExtra("catList");
        }
        getFact();
        factMultiTV1.setText(fact);
        factMultiTV2.setText(fact);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.trueMultiBtn1:
                calculateScore("true1");
                break;
            case R.id.trueMultiBtn2:
                calculateScore("false2");
                break;
            case R.id.falseMultiBtn1:
                calculateScore("false1");
                break;
            case R.id.falseMultiBtn2:
                calculateScore("true2");
                break;
        }
        getFact();
    }

    private void calculateScore(String btn){
        factMultiTV1.setVisibility(View.INVISIBLE);
        factMultiTV2.setVisibility(View.INVISIBLE);
        trueMultiBtn1.setClickable(false);
        trueMultiBtn2.setClickable(false);
        falseMultiBtn1.setClickable(false);
        falseMultiBtn2.setClickable(false);

        if(answer == 1){
            switch (btn){
                case "true1":
                    score1++;
                    rightMultiIV1.setVisibility(View.VISIBLE);
                    wrongMultiIV2.setVisibility(View.VISIBLE);
                    break;
                case "true2":
                    score2++;
                    wrongMultiIV1.setVisibility(View.VISIBLE);
                    rightMultiIV2.setVisibility(View.VISIBLE);
                    break;
                case "false1":
                    score2++;
                    wrongMultiIV1.setVisibility(View.VISIBLE);
                    rightMultiIV2.setVisibility(View.VISIBLE);
                    break;
                case "false2":
                    score1++;
                    rightMultiIV1.setVisibility(View.VISIBLE);
                    wrongMultiIV2.setVisibility(View.VISIBLE);
                    break;
            }
        }else if(answer == 0){
            switch (btn){
                case "true1":
                    score2++;
                    wrongMultiIV1.setVisibility(View.VISIBLE);
                    rightMultiIV2.setVisibility(View.VISIBLE);
                    break;
                case "true2":
                    score1++;
                    rightMultiIV1.setVisibility(View.VISIBLE);
                    wrongMultiIV2.setVisibility(View.VISIBLE);
                    break;
                case "false1":
                    score1++;
                    rightMultiIV1.setVisibility(View.VISIBLE);
                    wrongMultiIV2.setVisibility(View.VISIBLE);
                    break;
                case "false2":
                    score2++;
                    wrongMultiIV1.setVisibility(View.VISIBLE);
                    rightMultiIV2.setVisibility(View.VISIBLE);
                    break;
            }
        }
        mHandler.postDelayed(mRunnable, 1000);
        String scoreStr1 = String.format(score1 + " : " + score2);
        String scoreStr2 = String.format(score2 + " : " + score1);
        scoreTableTV1.setText(scoreStr1);
        scoreTableTV2.setText(scoreStr2);
    }

    private void getFact() {
        if (catFac) {
            ArrayList<String> factsList = new ArrayList<String>();
            ArrayList<Integer> answersList = new ArrayList<Integer>();
            cursor = db.gectFactOfCategory(catArgs);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        for (String string : cursor.getColumnNames()) {
                            if (string.equals(db.FACT_COLUMN_FACT))
                                factsList.add(cursor.getString(cursor.getColumnIndex(string)));
                            if (string.equals(db.FACT_COLUMN_ANSWER))
                                answersList.add(cursor.getInt(cursor.getColumnIndex(string)));
                        }
                    } while (cursor.moveToNext());
                }
            }
            id = random.nextInt(factsList.size());
            fact = factsList.get(id);
            answer = answersList.get(id);
            Log.d("myLogs", factsList.size() + "");
        } else {
            id = random.nextInt(15) + 1;
            cursor = db.getFact(id);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    for (String string : cursor.getColumnNames()) {
                        if (string.equals(db.FACT_COLUMN_FACT))
                            fact = cursor.getString(cursor.getColumnIndex(string));
                        if (string.equals(db.FACT_COLUMN_ANSWER))
                            answer = cursor.getInt(cursor.getColumnIndex(string));
                    }
                }
            }
        }
    }

    private MyRunnable mRunnable = new MyRunnable(this);

    private class MyHandler extends Handler {}

    private final MyHandler mHandler = new MyHandler();

    public class MyRunnable implements Runnable {
        private final WeakReference<Activity> mActivity;

        public MyRunnable(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            Activity activity = mActivity.get();
            if (activity != null) {
                Intent intent;
                if(score1 == 5){
                    intent = new Intent(MultiplayerActivity.this, GameOverMultiActivity.class);
                    intent.putExtra("winner", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                    finish();
                }else if(score2 == 5){
                    intent = new Intent(MultiplayerActivity.this, GameOverMultiActivity.class);
                    intent.putExtra("winner", 2);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                    finish();
                }else {
                    rightMultiIV1.setVisibility(View.INVISIBLE);
                    rightMultiIV2.setVisibility(View.INVISIBLE);
                    wrongMultiIV1.setVisibility(View.INVISIBLE);
                    wrongMultiIV2.setVisibility(View.INVISIBLE);
                    factMultiTV1.setText(fact);
                    factMultiTV2.setText(fact);
                    factMultiTV1.setVisibility(View.VISIBLE);
                    factMultiTV2.setVisibility(View.VISIBLE);
                    trueMultiBtn1.setClickable(true);
                    trueMultiBtn2.setClickable(true);
                    falseMultiBtn1.setClickable(true);
                    falseMultiBtn2.setClickable(true);
                }
            }
        }
    }


    private MyRunnable1 mRunnable1 = new MyRunnable1(this);

    private class MyHandler1 extends Handler {
    }

    private final MyHandler1 mHandler1 = new MyHandler1();

    public class MyRunnable1 implements Runnable {
        private final WeakReference<Activity> mActivity;

        public MyRunnable1(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            Activity activity = mActivity.get();
            if (activity != null) {
                playerRL.startAnimation(anim);
            }
        }
    }

}
