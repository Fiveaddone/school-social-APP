package com.example.scso.school_social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class HelloActivity extends Activity {
    private ImageView hello;
    private Timer timer;
    TimerTask Hello = new TimerTask() {
        @Override
        public void run() {
            timer.cancel();
            Intent intent = new Intent(HelloActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hello);
        hello = (ImageView)this.findViewById(R.id.hello);
        timer = new Timer(true);
        timer.schedule(Hello, 1000);



    }

}
