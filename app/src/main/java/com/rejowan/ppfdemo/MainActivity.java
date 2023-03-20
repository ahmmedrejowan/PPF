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




    }


}