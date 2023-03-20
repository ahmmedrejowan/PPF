package com.rejowan.ppf;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class SQUtils {

    private final String PREF_NAME = "ppf_sq_pref";
    private final Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private final String SQ_QUESTIONS = "ppf_sq_ques_pref";


    public SQUtils(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public ArrayList<String> getSecurityQuestions() {
        return new ArrayList<>(Arrays.asList(TextUtils.split(this.preferences.getString(SQ_QUESTIONS, ""), "‚‗‚")));
    }


    public void addSecurityQuestions(ArrayList<String> arrayList) {
        ArrayList<String> securityQuestions = getSecurityQuestions();
        for (String s : arrayList) {
            if (!securityQuestions.contains(s)) {
                securityQuestions.add(s);
            }
        }
        this.preferences.edit().putString(SQ_QUESTIONS, TextUtils.join("‚‗‚", (String[]) securityQuestions.toArray(new String[securityQuestions.size()]))).apply();
    }


}
