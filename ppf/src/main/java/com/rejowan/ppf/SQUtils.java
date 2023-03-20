package com.rejowan.ppf;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class SQUtils {

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final String SQ_QUESTIONS = "ppf_sq_ques_pref";
    public static final String SQ_SELECTED = "ppf_sq_ques_pref_selected";


    public SQUtils(Context context) {
        String PREF_NAME = "ppf_sq_pref";
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public ArrayList<String> getAllSQList() {
        return new ArrayList<>(Arrays.asList(TextUtils.split(this.preferences.getString(SQ_QUESTIONS, ""), "‚‗‚")));
    }


    public void setAllSQList(ArrayList<String> arrayList) {
        ArrayList<String> securityQuestions = getAllSQList();
        for (String s : arrayList) {
            if (!securityQuestions.contains(s)) {
                securityQuestions.add(s);
            }
        }
        this.editor.putString(SQ_QUESTIONS, TextUtils.join("‚‗‚", (String[]) securityQuestions.toArray(new String[securityQuestions.size()]))).apply();
    }

    public ArrayList<String> getSelectedSQList() {
        return new ArrayList<>(Arrays.asList(TextUtils.split(this.preferences.getString(SQ_SELECTED, ""), "‚‗‚")));
    }

    public void setSelectedSQList(ArrayList<String> arrayList) {
        this.editor.putString(SQ_SELECTED, "").apply();
        this.editor.putString(SQ_SELECTED, TextUtils.join("‚‗‚", (String[]) arrayList.toArray(new String[arrayList.size()]))).apply();
    }


    public void setSQAnswer(String question, String answer) {
        this.editor.putString("answer:" + question, stringToSHA256(answer)).apply();
    }

    public boolean checkSQAnswer(String question, String answer) {
        return this.preferences.getString("answer:" + question, "").equals(stringToSHA256(answer));
    }

    public static String stringToSHA256(String string) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                Log.e("TAG", "stringToSHA256: ", e1);
            }
            assert digest != null;
            digest.reset();
            return bin2hex(digest.digest(string.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }





}
