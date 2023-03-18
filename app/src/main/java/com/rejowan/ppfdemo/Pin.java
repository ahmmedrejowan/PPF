package com.rejowan.ppfdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rejowan.ppfdemo.databinding.ActivityPinBinding;

public class Pin extends AppCompatActivity {

    ActivityPinBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
        binding.indicatorDots.setCount(binding.pinLockView.getPinLength());






    }
}