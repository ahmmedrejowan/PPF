package com.rejowan.ppf;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityQuestion {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String PREF_NAME = "ppf_sq_pref";

    public SecurityQuestion(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }


}
