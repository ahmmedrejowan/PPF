package com.rejowan.ppfdemo;

import static com.rejowan.ppf.PatternLockView.stringToPattern;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rejowan.ppf.PPFSecurity;
import com.rejowan.ppf.PatternLockView;
import com.rejowan.ppfdemo.databinding.ActivityPatternBinding;

import java.util.List;

public class Pattern extends AppCompatActivity {


    ActivityPatternBinding binding;

    PPFSecurity ppfSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatternBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ppfSecurity = new PPFSecurity(this);

        if (!ppfSecurity.isPatternSet()){
            binding.patternLockView.setPattern(PatternLockView.PatternViewMode.AUTO_DRAW, stringToPattern(binding.patternLockView, "01234"));
        } else {
            Toast.makeText(this, "Enter your pattern", Toast.LENGTH_SHORT).show();
        }


        binding.patternLockView.addPatternLockListener(new PatternLockView.PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                if (ppfSecurity.isPatternSet()){
                    if (ppfSecurity.isPatternCorrect(PatternLockView.patternToString(binding.patternLockView, pattern))){
                        Toast.makeText(Pattern.this, "Logged In", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Pattern.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
                        binding.patternLockView.clearPattern();
                    }
                }  else {
                    ppfSecurity.setPattern(PatternLockView.patternToString(binding.patternLockView, pattern));
                    Toast.makeText(Pattern.this, "Pattern Set", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCleared() {

            }
        });


    }
}