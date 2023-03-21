package com.rejowan.ppf;

import android.app.Activity;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

public class FingerAuthy {

    Activity activity;
    BiometricPrompt.PromptInfo promptInfo;
    BiometricPrompt biometricPrompt;

    public FingerAuthy(Activity activity) {
        this.activity = activity;
    }

    public boolean hasBiometricSupport() {
        BiometricManager biometricManager = BiometricManager.from(activity);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
            default:
                return false;
        }
    }


    public void buildBiometricPrompt(String title, String subtitle, String description, String negativeButtonText) {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText(negativeButtonText)
                .build();

    }

    public void authenticate(BiometricPrompt.AuthenticationCallback authenticationCallback) {

        if (promptInfo == null) {
            throw new NullPointerException("PromptInfo is null. Please call buildBiometricPrompt() first");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            biometricPrompt = new BiometricPrompt((FragmentActivity) activity, activity.getMainExecutor(), authenticationCallback);
            biometricPrompt.authenticate(promptInfo);
        }
    }


    public void cancelAuthentication() {
        if (biometricPrompt != null) {
            biometricPrompt.cancelAuthentication();
        }
    }

}
