package com.rejowan.ppfdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rejowan.ppf.SQUtils;
import com.rejowan.ppfdemo.databinding.ActivitySecurityQuestionsBinding;

import java.util.ArrayList;

public class SecurityQuestions extends AppCompatActivity {

    ActivitySecurityQuestionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SQUtils sqUtils = new SQUtils(this);

        Log.e("TAG", "onCreate 1: " + sqUtils.getSecurityQuestions());


        ArrayList<String> questions = new ArrayList<>();
        questions.add("What is your mother's maiden name?");
        questions.add("What is your pet's name?");
        questions.add("What is your favorite color?");
        questions.add("What is your favorite food?");
        questions.add("What is your favorite movie?");
        questions.add("What is your favorite book?");
        questions.add("What is your favorite song?");

        sqUtils.addSecurityQuestions(questions);



        Log.e("TAG", "onCreate 2: " + sqUtils.getSecurityQuestions());






    }
}