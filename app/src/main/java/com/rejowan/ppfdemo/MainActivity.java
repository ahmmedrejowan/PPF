package com.rejowan.ppfdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rejowan.ppfdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pin.setOnClickListener(v -> startActivity(new Intent(this, Pin.class)));

        binding.pattern.setOnClickListener(v -> startActivity(new Intent(this, Pattern.class)));

        binding.reset.setOnClickListener(v -> startActivity(new Intent(this, SecurityQuestions.class)));

        binding.finger.setOnClickListener(v -> startActivity(new Intent(this, FingerPrint.class)));


//        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
//        binding.indicatorDots.setCount(binding.pinLockView.getPinLength());
//
//
//
//
//
//        binding.patternLockView.setPattern(PatternLockView.PatternViewMode.AUTO_DRAW, stringToPattern(binding.patternLockView, "01234"));
//
//        binding.patternLockView.addPatternLockListener(new PatternLockView.PatternLockViewListener() {
//            @Override
//            public void onStarted() {
//
//            }
//
//            @Override
//            public void onProgress(List<PatternLockView.Dot> progressPattern) {
//
//            }
//
//            @Override
//            public void onComplete(List<PatternLockView.Dot> pattern) {
//
//                if (PatternLockView.patternToString(binding.patternLockView, pattern).equals("0367")) {
//
//                    Log.e("TAG", "success: " + PatternLockView.patternToString(binding.patternLockView, pattern));
//
//                    binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
//
//                    new Handler().postDelayed(() -> Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show(), 1000);
//
//
//                } else {
//                    Log.e("TAG", "failed: " + PatternLockView.patternToString(binding.patternLockView, pattern));
//
//                    binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
//
//                    new Handler().postDelayed(() -> {
//
//                        Toast.makeText(MainActivity.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
//
//                        binding.patternLockView.clearPattern();
//
//                    }, 1000);
//
//
//                }
//
//                Log.e("TAG", "onComplete: " + PatternLockView.patternToString(binding.patternLockView, pattern));
//                Log.e("TAG", "onComplete: " + pattern);
//
//            }
//
//            @Override
//            public void onCleared() {
//
//            }
//        });


    }


}