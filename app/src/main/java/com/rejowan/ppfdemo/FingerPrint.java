package com.rejowan.ppfdemo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rejowan.ppf.FingerAuthy;
import com.rejowan.ppfdemo.databinding.ActivityFingerPrintBinding;

public class FingerPrint extends AppCompatActivity {


    ActivityFingerPrintBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFingerPrintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FingerAuthy fingerAuthy = new FingerAuthy(this);
        if (fingerAuthy.hasBiometricSupport()) {
            fingerAuthy.buildBiometricPrompt("Biometric Authentication", "Please authenticate to continue", "Use your face or fingerprint to authenticate", "Cancel");
            fingerAuthy.authenticate(new FingerAuthy.AuthenticationCallback() {
                @Override
                public void onAuthenticationError() {

                }

                @Override
                public void onAuthenticationSucceeded() {

                }

                @Override
                public void onAuthenticationFailed() {

                }
            });

        } else {
            Toast.makeText(this, "No Biometric Support", Toast.LENGTH_SHORT).show();
        }

    }

}