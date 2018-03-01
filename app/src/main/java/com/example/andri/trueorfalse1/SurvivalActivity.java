package com.example.andri.trueorfalse1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SurvivalActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus;
    MediaPlayer mediaFalsePlayer;
    MediaPlayer mediaTruePlayer;

    ImageView survivalBackIV;
    ImageView lifeIV1, lifeIV2, lifeIV3, lifeIV4, lifeIV5, rightSurIV, wrongSurIV;
    TextView scoreSurvivalTV;

    TextView factSurvivalTV, skipTV;
    Button skipSurvivalBtn, trueSurvivalBtn, falseSurvivalBtn;

    DB db;
    Cursor cursor;
    Animation anim;
    Animation anim2;

    private int id;
    Random random = new Random();
    String[] catArgs;

    boolean catFac = false;
    private String fact = "";
    private int answer = 0;
    private int score = 0;
    private int life = 5;
    private int skip = 4;
    private int bestSurvivalScore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_survival);
        loadSoundText();
        mediaFalsePlayer = MediaPlayer.create(this, R.raw.false_answer);
        mediaTruePlayer = MediaPlayer.create(this, R.raw.true_answer);

        survivalBackIV = (ImageView) findViewById(R.id.survivalBack);
        skipSurvivalBtn = (Button) findViewById(R.id.skipSurvivalBtn);
        trueSurvivalBtn = (Button) findViewById(R.id.trueSurvivalBtn);
        falseSurvivalBtn = (Button) findViewById(R.id.falseSurvivalBtn);
        survivalBackIV.setOnClickListener(this);
        skipSurvivalBtn.setOnClickListener(this);
        trueSurvivalBtn.setOnClickListener(this);
        falseSurvivalBtn.setOnClickListener(this);

        factSurvivalTV = (TextView) findViewById(R.id.factSurvivalTV);
        scoreSurvivalTV = (TextView) findViewById(R.id.scoreSurvivalTV);
        skipTV = (TextView) findViewById(R.id.skipTV);

        lifeIV1 = (ImageView) findViewById(R.id.lifeIV1);
        lifeIV2 = (ImageView) findViewById(R.id.lifeIV2);
        lifeIV3 = (ImageView) findViewById(R.id.lifeIV3);
        lifeIV4 = (ImageView) findViewById(R.id.lifeIV4);
        lifeIV5 = (ImageView) findViewById(R.id.lifeIV5);
        rightSurIV = (ImageView) findViewById(R.id.rightSurvivalIV);
        wrongSurIV = (ImageView) findViewById(R.id.wrongSurvivalIV);

        db = new DB(this);
        db.open();

        catFac = false;
        score = 0;
        life = 5;
        skip = 4;

        Intent intent = getIntent();
        if (intent.getStringArrayExtra("catList") != null) {
            catFac = true;
            catArgs = intent.getStringArrayExtra("catList");
        }
        anim = AnimationUtils.loadAnimation(this, R.anim.fact_slide_right_in);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.fact_slide_left_out);
        getFact();
        factSurvivalTV.setText(fact);
//        factSurvivalTV.startAnimation(anim);
    }

    private void loadSoundText() {
        sPref = getSharedPreferences("mySound", MODE_APPEND);
        String savedtext = sPref.getString(SAVED_TEXT, "");
        soundStatus = savedtext;
    }

    private MyRunnable mRunnable = new MyRunnable(this);
    private MyRunnable1 mRunnable1 = new MyRunnable1(this);

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.survivalBack:
//                intent = new Intent(this, ModsActivity.class);
//                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
            case R.id.trueSurvivalBtn:
                if (answer == 1) {
                    score++;
                    scoreSurvivalTV.setText(String.valueOf(score));
                    rightSurIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaTruePlayer.start();
                } else if (answer == 0) {
                    life--;
                    setHeartBroken();
                    wrongSurIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaFalsePlayer.start();
                }
                if (life != 0)
                    getFact();
                trueSurvivalBtn.setClickable(false);
                falseSurvivalBtn.setClickable(false);
                mHandler.postDelayed(mRunnable, 1000);
                mHandler1.postDelayed(mRunnable1, 800);
                break;
            case R.id.falseSurvivalBtn:
                if (answer == 0) {
                    score++;
                    scoreSurvivalTV.setText(String.valueOf(score));
                    rightSurIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaTruePlayer.start();
                } else if (answer == 1) {
                    life--;
                    setHeartBroken();
                    wrongSurIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaFalsePlayer.start();
                }
                if (life != 0)
                    getFact();
                trueSurvivalBtn.setClickable(false);
                falseSurvivalBtn.setClickable(false);
                mHandler.postDelayed(mRunnable, 1000);
                mHandler1.postDelayed(mRunnable1, 800);
                break;
            case R.id.skipSurvivalBtn:
                if (skip > 0) {
                    getFact();
                    skip--;
                    skipTV.setText(String.valueOf(skip));
                    mHandler.postDelayed(mRunnable, 300);
                    mHandler1.postDelayed(mRunnable1, 100);
//                    factSurvivalTV.setText(fact);
                } else {
                    Toast.makeText(this, "Пропуск ходу вичерпано", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
//        factSurvivalTV.startAnimation(anim2);
    }

    private void getBestScore() {
        cursor = db.getBestScore("Survival");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (String string : cursor.getColumnNames()) {
                    if (string.equals(db.BEST_SCORE_COLUMN_SCORE))
                        bestSurvivalScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(string)));
                    Log.d("myLogs", "" + bestSurvivalScore);
                }
            }
        }
    }

    private void setHeartBroken() {
        int imageSize;
        float scale;
        LinearLayout.LayoutParams lp;
        scale = getResources().getDisplayMetrics().density;
        imageSize = (int) (32 * scale + 0.5f);
        lp = new LinearLayout.LayoutParams(imageSize, imageSize);
        lp.setMargins(0, 0, 8, 0);

        switch (life) {
            case 4:
                lifeIV1.setImageResource(R.drawable.heartbroken);
                lifeIV1.setLayoutParams(lp);
                break;
            case 3:
                lifeIV2.setImageResource(R.drawable.heartbroken);
                lifeIV2.setLayoutParams(lp);
                Animation animHeart = AnimationUtils.loadAnimation(this, R.anim.scale_in);
                lifeIV2.startAnimation(animHeart);
                break;
            case 2:
                lifeIV3.setImageResource(R.drawable.heartbroken);
                lifeIV3.setLayoutParams(lp);
                break;
            case 1:
                lifeIV4.setImageResource(R.drawable.heartbroken);
                lifeIV4.setLayoutParams(lp);
                break;
            case 0:
                lifeIV5.setImageResource(R.drawable.heartbroken);
                getBestScore();
                finishGame();
                lifeIV5.setLayoutParams(lp);
                break;
        }
    }

    private void finishGame() {
        Intent intent = new Intent(this, GameOverSurvivalActivity.class);
        intent.putExtra("score", String.valueOf(score));
        if (score > bestSurvivalScore) {
            intent.putExtra("bestScore", String.valueOf(score));
            intent.putExtra("newrecord", true);
        } else {
            intent.putExtra("bestScore", String.valueOf(bestSurvivalScore));
            intent.putExtra("newrecord", false);
        }
        if (catFac)
            intent.putExtra("category", true);
        else
            intent.putExtra("category", false);
        saveBestScore();
        db.close();
        cursor.close();
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        finish();
    }

    private void saveBestScore() {
        if (!catFac && bestSurvivalScore < score) {
            db.updateBestScore(score, "Survival");
        }
    }

    private class MyHandler extends Handler {
    }

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
                rightSurIV.setVisibility(View.INVISIBLE);
                wrongSurIV.setVisibility(View.INVISIBLE);
                factSurvivalTV.setText(fact);
                factSurvivalTV.startAnimation(anim);
                trueSurvivalBtn.setClickable(true);
                falseSurvivalBtn.setClickable(true);
            }
        }
    }


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
                factSurvivalTV.startAnimation(anim2);
            }
        }
    }

}
