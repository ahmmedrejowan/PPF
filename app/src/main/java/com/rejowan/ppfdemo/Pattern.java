package com.rejowan.ppfdemo;

import static com.rejowan.ppf.PatternLockView.stringToPattern;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rejowan.ppf.PatternLockView;
import com.rejowan.ppfdemo.databinding.ActivityPatternBinding;

import java.util.List;

public class Pattern extends AppCompatActivity {


    ActivityPatternBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatternBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.patternLockView.setPattern(PatternLockView.PatternViewMode.AUTO_DRAW, stringToPattern(binding.patternLockView, "01234"));

        binding.patternLockView.addPatternLockListener(new PatternLockView.PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                if (PatternLockView.patternToString(binding.patternLockView, pattern).equals("0367")) {

                    Log.e("TAG", "success: " + PatternLockView.patternToString(binding.patternLockView, pattern));

                    binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);

                    new Handler().postDelayed(() -> Toast.makeText(Pattern.this, "Logged In", Toast.LENGTH_SHORT).show(), 1000);


                } else {
                    Log.e("TAG", "failed: " + PatternLockView.patternToString(binding.patternLockView, pattern));

                    binding.patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);

                    new Handler().postDelayed(() -> {

                        Toast.makeText(Pattern.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();

                        binding.patternLockView.clearPattern();

                    }, 1000);


                }

                Log.e("TAG", "onComplete: " + PatternLockView.patternToString(binding.patternLockView, pattern));
                Log.e("TAG", "onComplete: " + pattern);

            }

            @Override
            public void onCleared() {

            }
        });


    }
}