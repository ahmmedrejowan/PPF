package com.rejowan.ppfdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rejowan.ppf.SQUtils;
import com.rejowan.ppfdemo.databinding.ActivitySecurityQuestionsBinding;

import java.util.ArrayList;

public class SecurityQuestions extends AppCompatActivity {

    ActivitySecurityQuestionsBinding binding;
    SQUtils sqUtils;

    ArrayList<String> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sqUtils = new SQUtils(this);

        SetupQuestions();

        SetupQA();

        SetupVerify();

    }

    private void SetupQuestions() {

        // adding questions to the list
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("What is your mother's maiden name?");
        stringArrayList.add("What is your pet's name?");
        stringArrayList.add("What is your favorite color?");
        stringArrayList.add("What is your favorite food?");
        stringArrayList.add("What is your favorite movie?");
        stringArrayList.add("What is your favorite book?");
        stringArrayList.add("What is your favorite song?");
        sqUtils.setAllSQList(stringArrayList);

        questions.add("Please select a question");
        questions.addAll(sqUtils.getAllSQList());


    }

    private void SetupQA() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, questions);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
        binding.spinner1.setAdapter(spinnerArrayAdapter);


        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    // remove the selected item from the list and set adapter for spinner 2
                    ArrayList<String> questions2 = new ArrayList<>(questions);
                    questions2.remove(position);
                    ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(SecurityQuestions.this, R.layout.spinner_item, questions2);
                    spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item_drop);
                    binding.spinner2.setAdapter(spinnerArrayAdapter2);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.submit.setOnClickListener(v -> {

            // get the selected items from the spinners

            if (binding.spinner1.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select a question", Toast.LENGTH_SHORT).show();
                return;
            }

            if (binding.spinner2.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Please select a question", Toast.LENGTH_SHORT).show();
                return;
            }


            String question1 = binding.spinner1.getSelectedItem().toString();
            String question2 = binding.spinner2.getSelectedItem().toString();

            String answer1 = binding.answer1.getText().toString();
            String answer2 = binding.answer2.getText().toString();

            if (answer1.isEmpty()) {
                binding.answer1.setError("Please enter an answer");
            } else if (answer2.isEmpty()) {
                binding.answer2.setError("Please enter an answer");
            } else {

                ArrayList<String> selectedQuestions = new ArrayList<>();
                selectedQuestions.add(question1);
                selectedQuestions.add(question2);
                sqUtils.setSelectedSQList(selectedQuestions);

                sqUtils.setSQAnswer(question1, answer1);
                sqUtils.setSQAnswer(question2, answer2);

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                SetupVerify();

            }


        });
    }

    private void SetupVerify() {

        ArrayList<String> userSelectedQuestions = sqUtils.getSelectedSQList();

        binding.q1.setText(userSelectedQuestions.get(0));
        binding.q2.setText(userSelectedQuestions.get(1));


        binding.submit2.setOnClickListener(v -> {

            String answer1 = binding.answer3.getText().toString();
            String answer2 = binding.answer4.getText().toString();

            if (answer1.isEmpty()) {
                binding.answer1.setError("Please enter an answer");
            } else if (answer2.isEmpty()) {
                binding.answer2.setError("Please enter an answer");
            } else {

                if (sqUtils.checkSQAnswer(userSelectedQuestions.get(0), answer1) && sqUtils.checkSQAnswer(userSelectedQuestions.get(1), answer2)) {
                    Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Wrong answers", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }


}