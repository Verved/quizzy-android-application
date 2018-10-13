package com.example.msq.quizzy;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class launch_screen extends AppCompatActivity {

    TextView quizzyLauncherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        quizzyLauncherText = findViewById(R.id.launcherQuizzyText);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/optima.ttf");
        quizzyLauncherText.setTypeface(customFont);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(launch_screen.this, registerPg.class);
                startActivity(i);
                finish();
            }
        }, 1000
        );
    }
}
