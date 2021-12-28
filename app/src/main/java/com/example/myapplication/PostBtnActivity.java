package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class PostBtnActivity extends AppCompatActivity {

    TextView txtvw;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_btn);

        txtvw = findViewById(R.id.txtvw);
        animationView = findViewById(R.id.animationView);

        txtvw.setText(getIntent().getStringExtra("TextBox"));

    }
}