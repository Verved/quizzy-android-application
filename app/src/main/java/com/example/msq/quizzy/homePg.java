package com.example.msq.quizzy;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class homePg extends AppCompatActivity {

    TextView swiftText, cppText, cLangText, javaText, pythonText, javascriptText, phpText, rubyText, toolbartitle;

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pg);

        swiftText = findViewById(R.id.swiftText);
        cppText = findViewById(R.id.cppText);
        cLangText = findViewById(R.id.cText);
        javaText = findViewById(R.id.javaText);
        pythonText = findViewById(R.id.pythonText);
        javascriptText = findViewById(R.id.javascriptText);
        phpText = findViewById(R.id.phpText);
        rubyText = findViewById(R.id.rubyText);
        toolbartitle = findViewById(R.id.toolbar_title);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/feltmark.ttf");
        swiftText.setTypeface(customFont);
        cppText.setTypeface(customFont);
        cLangText.setTypeface(customFont);
        javaText.setTypeface(customFont);
        phpText.setTypeface(customFont);
        javascriptText.setTypeface(customFont);
        phpText.setTypeface(customFont);
        rubyText.setTypeface(customFont);

        Typeface customFont1 = Typeface.createFromAsset(getAssets(), "fonts/optima.ttf");
        toolbartitle.setTypeface(customFont1);

        toolbar = findViewById(R.id.action_bar);
        toolbar.setTitle("QUIZZY");
        setSupportActionBar(toolbar);

    }


}
