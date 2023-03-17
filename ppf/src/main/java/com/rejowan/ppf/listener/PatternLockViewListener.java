package com.rejowan.ppf.listener;


import com.rejowan.ppf.PatternLockView;

import java.util.List;


public interface PatternLockViewListener {


    void onStarted();


    void onProgress(List<PatternLockView.Dot> progressPattern);


    void onComplete(List<PatternLockView.Dot> pattern);


    void onCleared();
}