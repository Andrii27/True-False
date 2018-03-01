package com.example.andri.trueorfalse1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverSurvivalActivity extends AppCompatActivity implements View.OnClickListener{

    TextView resultTV, bestScoreTV, bestScoreSurvTV;
    Button playAgainBtn, mainMenuBtn;
    ImageView newRecordIV, bestScoreIV;

    boolean catSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over_survival);

        newRecordIV = (ImageView) findViewById(R.id.newRecordSurvivalIV);
        bestScoreIV = (ImageView) findViewById(R.id.bestScoreSurvivalIV);
        bestScoreSurvTV = (TextView) findViewById(R.id.bestScoreTV);
        resultTV = (TextView) findViewById(R.id.resultSurvivalTV);
        bestScoreTV = (TextView) findViewById(R.id.bestScoreSurvivalTV);
        playAgainBtn = (Button) findViewById(R.id.playAgainSurvivalBtn);
        mainMenuBtn = (Button) findViewById(R.id.mainMenuSurvivalBtn);
        playAgainBtn.setOnClickListener(this);
        mainMenuBtn.setOnClickListener(this);

        Intent intent = getIntent();
        resultTV.setText(intent.getStringExtra("score"));
        bestScoreTV.setText(intent.getStringExtra("bestScore"));

        if(intent.getBooleanExtra("newrecord", true)){
            newRecordIV.setVisibility(View.VISIBLE);
        }
        if(intent.getBooleanExtra("category", false)){
            bestScoreTV.setVisibility(View.INVISIBLE);
            newRecordIV.setVisibility(View.INVISIBLE);
            bestScoreSurvTV.setVisibility(View.INVISIBLE);
            bestScoreIV.setVisibility(View.INVISIBLE);
            catSelect = true;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.playAgainSurvivalBtn:
                if(catSelect)
                    intent = new Intent(this, CategoryActivity.class);
                else
                    intent = new Intent(this, SurvivalActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mainMenuSurvivalBtn:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
