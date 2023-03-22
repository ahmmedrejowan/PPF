package com.rejowan.ppfdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.rejowan.ppf.PPFSecurity;
import com.rejowan.ppfdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    PPFSecurity ppfSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pin.setOnClickListener(v -> startActivity(new Intent(this, Pin.class)));

        binding.pattern.setOnClickListener(v -> startActivity(new Intent(this, Pattern.class)));

        binding.reset.setOnClickListener(v -> startActivity(new Intent(this, SecurityQuestions.class)));

        binding.finger.setOnClickListener(v -> startActivity(new Intent(this, FingerPrint.class)));


        ppfSecurity = new PPFSecurity(this);


        binding.pinSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ppfSecurity.setPinEnabled(true);
                } else {
                    ppfSecurity.setPinEnabled(false);
                }
                checkSettings();
            }
        });

        binding.patternSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ppfSecurity.setPatternEnabled(true);
                } else {
                    ppfSecurity.setPatternEnabled(false);
                }
                checkSettings();
            }
        });

        binding.fingerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ppfSecurity.enableFingerPrint(true);
                } else {
                    ppfSecurity.enableFingerPrint(false);
                }

            }
        });


        binding.sqSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ppfSecurity.setSQ(true);
                } else {
                    ppfSecurity.setSQ(false);
                }

            }
        });


        binding.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ppfSecurity.setSecurityType(PPFSecurity.PPFSecurityType.PIN);
                } else {
                    ppfSecurity.setSecurityType(PPFSecurity.PPFSecurityType.PATTERN);
                }

            }
        });


        binding.globalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ppfSecurity.setGlobalToggle(true);
                } else {
                    ppfSecurity.setGlobalToggle(false);
                }
            }
        });
    }

    private void checkSettings() {

        if (ppfSecurity.isPinEnabled() || ppfSecurity.isPatternEnabled()){
            binding.globalSwitch.setChecked(true);
            ppfSecurity.setGlobalToggle(true);
            binding.fingerSwitch.setEnabled(true);
            binding.sqSwitch.setEnabled(true);
            binding.toggleSwitch.setEnabled(true);
        }

        if (!ppfSecurity.isPinEnabled() && !ppfSecurity.isPatternEnabled()){
            binding.globalSwitch.setChecked(false);
            binding.fingerSwitch.setEnabled(false);
            binding.sqSwitch.setEnabled(false);
            binding.toggleSwitch.setEnabled(false);

            ppfSecurity.setGlobalToggle(false);

        }


    }


}