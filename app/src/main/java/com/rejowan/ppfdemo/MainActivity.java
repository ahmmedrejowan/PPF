package com.rejowan.ppfdemo;

import static com.rejowan.ppf.PatternLockView.stringToPattern;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rejowan.ppf.PatternLockView;
import com.rejowan.ppfdemo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);



        binding.patternLockView.setPattern(PatternLockView.PatternViewMode.AUTO_DRAW, stringToPattern(binding.patternLockView, "0123"));

        binding.patternLockView.addPatternLockListener(new PatternLockView.PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {


                Log.e("TAG", "onComplete: " + pattern.toString()  );

            }

            @Override
            public void onCleared() {

            }
        });



    }


}