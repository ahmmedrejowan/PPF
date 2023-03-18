package com.rejowan.ppfdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rejowan.ppfdemo.databinding.ActivityUnlockedBinding;

public class Unlocked extends AppCompatActivity {

    ActivityUnlockedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnlockedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}