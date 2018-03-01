package com.example.andri.trueorfalse1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView timerTV, scoreTimeTV, factTimeTV;
    Button trueTimeBtn, falseTimeBtn;
    ImageView backTimeIV, rightTimeIV, wrongTimeIV;
    SharedPreferences sPref;
    final String SAVED_TEXT = "sound";
    String soundStatus;
    MediaPlayer mediaFalsePlayer;
    MediaPlayer mediaTruePlayer;
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    private MyRunnable mRunnable = new MyRunnable(this);

    DB db;
    Cursor cursor;

    private int id;
    Random random = new Random();
    String[] catArgs;

    boolean catFac = false;
    private String fact = "";
    private int answer = 0;
    private int timer = 20;
    private int score = 0;
    private int bestTimeScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_time);

        loadSoundText();
        mediaFalsePlayer = MediaPlayer.create(this, R.raw.false_answer);
        mediaTruePlayer = MediaPlayer.create(this, R.raw.true_answer);

        timerTV = (TextView) findViewById(R.id.timerTV);
        scoreTimeTV = (TextView) findViewById(R.id.scoreTimeTV);
        factTimeTV = (TextView) findViewById(R.id.factTimeTV);
        trueTimeBtn = (Button) findViewById(R.id.trueTimeBtn);
        falseTimeBtn = (Button) findViewById(R.id.falseTimeBtn);
        rightTimeIV = (ImageView) findViewById(R.id.rightTimeIV);
        wrongTimeIV = (ImageView) findViewById(R.id.wrongTimeIV);
        backTimeIV = (ImageView) findViewById(R.id.timeBack);
        backTimeIV.setOnClickListener(this);
        trueTimeBtn.setOnClickListener(this);
        falseTimeBtn.setOnClickListener(this);

        timer = 20;

        db = new DB(this);
        db.open();

        Intent intent = getIntent();
        if (intent.getStringArrayExtra("catList") != null) {
            catFac = true;
            catArgs = intent.getStringArrayExtra("catList");
        }
        getFact();
        factTimeTV.setText(fact);

        if (mTimer != null)
            mTimer.cancel();
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 1000);
    }

    private void loadSoundText() {
        sPref = getSharedPreferences("mySound", MODE_APPEND);
        String savedtext = sPref.getString(SAVED_TEXT, "");
        soundStatus = savedtext;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeBack:
//                Intent intent = new Intent(this, ModsActivity.class);
//                startActivity(intent);
                mTimer.cancel();
                finish();
                break;
            case R.id.trueTimeBtn:
                if (answer == 1) {
                    score++;
                    scoreTimeTV.setText(String.valueOf(score));
                    rightTimeIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaTruePlayer.start();
                } else if (answer == 0) {
                    wrongTimeIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaFalsePlayer.start();
                }
                getFact();
                trueTimeBtn.setClickable(false);
                falseTimeBtn.setClickable(false);
                mHandler.postDelayed(mRunnable, 1000);
                break;
            case R.id.falseTimeBtn:
                if (answer == 0) {
                    score++;
                    scoreTimeTV.setText(String.valueOf(score));
                    rightTimeIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaTruePlayer.start();
                } else if (answer == 1) {
                    wrongTimeIV.setVisibility(View.VISIBLE);
                    if (soundStatus.equals("on"))
                        mediaFalsePlayer.start();
                }
                getFact();
                trueTimeBtn.setClickable(false);
                falseTimeBtn.setClickable(false);
                mHandler.postDelayed(mRunnable, 1000);
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
    }

    private void getBestScore() {
        cursor = db.getBestScore("Time");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (String string : cursor.getColumnNames()) {
                    if (string.equals(db.BEST_SCORE_COLUMN_SCORE))
                        bestTimeScore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(string)));
                    Log.d("myLogs", "" + bestTimeScore);
                }
            }
        }
    }

    private void saveBestScore() {
        if (bestTimeScore < score) {
            db.updateBestScore(score, "Time");
        }
    }

    private void finishGame() {
        Intent intent = new Intent(this, GameOverTimeActivity.class);
        intent.putExtra("score", String.valueOf(score));
        if (score > bestTimeScore) {
            intent.putExtra("bestScore", String.valueOf(score));
            intent.putExtra("newrecord", true);
        } else {
            intent.putExtra("bestScore", String.valueOf(bestTimeScore));
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

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerTV.setText(String.valueOf(timer--));
                    if (timer == -1) {
                        trueTimeBtn.setClickable(false);
                        falseTimeBtn.setClickable(false);
                        mTimer.cancel();
                        mTimer = null;
                        getBestScore();
                        finishGame();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
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
                rightTimeIV.setVisibility(View.INVISIBLE);
                wrongTimeIV.setVisibility(View.INVISIBLE);
                factTimeTV.setText(fact);
                trueTimeBtn.setClickable(true);
                falseTimeBtn.setClickable(true);
            }
        }
    }

}
