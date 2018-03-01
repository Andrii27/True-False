package com.example.andri.trueorfalse1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverTimeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTV, bestScoreTimeTV, bestScoreTitleTV;
    Button playAgainBtn, mainMenuBtn;
    ImageView newRecordIV, bestScoreTimeIV;

    boolean catSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over_time);

        bestScoreTimeIV = (ImageView) findViewById(R.id.bestScoreTimeIV);
        bestScoreTitleTV = (TextView) findViewById(R.id.bestScoreTitleTV);
        newRecordIV = (ImageView) findViewById(R.id.newRecordTimeIV);
        resultTV = (TextView) findViewById(R.id.resultTimeTV);
        bestScoreTimeTV = (TextView) findViewById(R.id.bestScoreTimeTV);
        playAgainBtn = (Button) findViewById(R.id.playAgainTimeBtn);
        mainMenuBtn = (Button) findViewById(R.id.mainMenuTimeBtn);
        playAgainBtn.setOnClickListener(this);
        mainMenuBtn.setOnClickListener(this);

        Intent intent = getIntent();
        resultTV.setText(intent.getStringExtra("score"));
        bestScoreTimeTV.setText(intent.getStringExtra("bestScore"));

        if (intent.getBooleanExtra("newrecord", true)) {
            newRecordIV.setVisibility(View.VISIBLE);
        }
        if (intent.getBooleanExtra("category", false)) {
            bestScoreTitleTV.setVisibility(View.INVISIBLE);
            newRecordIV.setVisibility(View.INVISIBLE);
            bestScoreTimeTV.setVisibility(View.INVISIBLE);
            bestScoreTimeIV.setVisibility(View.INVISIBLE);
            catSelect = true;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.playAgainTimeBtn:
                if (catSelect)
                    intent = new Intent(this, CategoryActivity.class);
                else
                    intent = new Intent(this, TimeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mainMenuTimeBtn:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
