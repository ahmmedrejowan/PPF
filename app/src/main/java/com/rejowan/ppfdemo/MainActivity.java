package com.rejowan.ppfdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rejowan.ppf.PatternLockView;
import com.rejowan.ppf.listener.PatternLockViewListener;
import com.rejowan.ppf.utils.PatternLockUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        PatternLockView.Dot[][] dot1 = new PatternLockView.Dot[0][0];
//        PatternLockView.Dot[][] dot2 = new PatternLockView.Dot[1][0];
//        PatternLockView.Dot[][] dot3 = new PatternLockView.Dot[2][0];
//        PatternLockView.Dot[][] dot4 = new PatternLockView.Dot[2][1];
//
//
//
//        List<PatternLockView.Dot[][]> pattern = new ArrayList<>();
//        pattern.add(dot1);
//        pattern.add(dot2);
//        pattern.add(dot3);
//        pattern.add(dot4);




        PatternLockView patternLockView = findViewById(R.id.pattern_lock_view);


        patternLockView.setPattern(PatternLockView.PatternViewMode.AUTO_DRAW, PatternLockUtils.stringToPattern(patternLockView, "0123"));


        patternLockView.addPatternLockListener(new PatternLockViewListener() {
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