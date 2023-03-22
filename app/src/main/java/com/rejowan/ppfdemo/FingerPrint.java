package com.rejowan.ppfdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

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
        if (fingerAuthy.hasBiometricSupport()){
            fingerAuthy.buildBiometricPrompt("Biometric Authentication","Please authenticate to continue","Use your face or fingerprint to authenticate","Cancel");
            fingerAuthy.authenticate(new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    startActivity(new Intent(FingerPrint.this, Unlocked.class));

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                }
            });

        } else {
            Toast.makeText(this, "No Biometric Support", Toast.LENGTH_SHORT).show();
        }

    }

}