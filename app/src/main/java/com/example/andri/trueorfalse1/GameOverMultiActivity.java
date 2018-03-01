package com.example.andri.trueorfalse1;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

public class GameOverMultiActivity extends AppCompatActivity {

    TextView resTV1, resTV2;
    ImageView resIV1, resIV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over_multi);

        resTV1 = (TextView) findViewById(R.id.resultTV1);
        resTV2 = (TextView) findViewById(R.id.resultTV2);
        resIV1 = (ImageView) findViewById(R.id.resIV1);
        resIV2 = (ImageView) findViewById(R.id.resIV2);

        Intent intent = getIntent();
        if(intent.getIntExtra("winner", 5) == 1){
            resIV1.setImageResource(R.drawable.winner);
            resIV2.setImageResource(R.drawable.loser);
            resTV1.setText("Перемога!");
            resTV2.setText("Поразка!");
            resTV1.setTextColor(getResources().getColor(R.color.trueColor));
            resTV2.setTextColor(getResources().getColor(R.color.falseColor));
        }else if(intent.getIntExtra("winner", 5) == 2){
            resIV2.setImageResource(R.drawable.winner);
            resIV1.setImageResource(R.drawable.loser);
            resTV2.setText("Перемога!");
            resTV1.setText("Поразка!");
            resTV2.setTextColor(getResources().getColor(R.color.trueColor));
            resTV1.setTextColor(getResources().getColor(R.color.falseColor));
        }
        mHandler.postDelayed(mRunnable, 5000);
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
//                Intent intent = new Intent(GameOverMultiActivity.this, ModsActivity.class);
//                startActivity(intent);
                finish();
            }
        }
    }
}
