package com.example.mealtr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mealtr.calorieCalculator.BMICalculator;
import com.example.mealtr.calorieCalculator.CalorieCalculator;
import com.example.mealtr.database.MenuViewModel;
import com.example.mealtr.mealGenerator.GenerateMealAsync;

import java.util.logging.Logger;

public class UserDetailsFragment extends Fragment {
    Activity context;
    MenuViewModel menuViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();

        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // GENDER
        Spinner inputGender = context.findViewById(R.id.inputGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputGender.setAdapter(adapter);
        inputGender.setSelection(0, false);

        // ACTIVITY
        Spinner inputActivity = context.findViewById(R.id.inputActive);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireContext(), R.array.activityLevel, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputActivity.setAdapter(adapter2);
        inputActivity.setSelection(0, false);

        // GENERATE
        Button btnGenerateMeal = context.findViewById(R.id.btn_generate_meal);
        btnGenerateMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputAge = getActivity().findViewById(R.id.inputUserAge);
                EditText inputHeight = getActivity().findViewById(R.id.inputHeight);
                EditText inputCurrentWeight = getActivity().findViewById(R.id.inputCurrentWeight);
                EditText inputTargetWeight = getActivity().findViewById(R.id.inputTargetWeight);
                Spinner inputGender = getActivity().findViewById(R.id.inputGender);
                Spinner inputActive = getActivity().findViewById(R.id.inputActive);

                int age = Integer.valueOf(inputAge.getText().toString());
                int height = Integer.valueOf(inputHeight.getText().toString());
                int currentWeight = Integer.valueOf(inputCurrentWeight.getText().toString());
                int targetWeight =  Integer.valueOf(inputTargetWeight.getText().toString());
                String gender = inputGender.getSelectedItem().toString();
                String activity = inputActive.getSelectedItem().toString();

                BMICalculator bmiCalculator = new BMICalculator();

                if(bmiCalculator.isValid(Double.valueOf(inputHeight.getText().toString()), Double.valueOf(inputCurrentWeight.getText().toString()),Double.valueOf(inputTargetWeight.getText().toString()))) {
                    FragmentManager manager = getParentFragmentManager();

                    CalorieCalculator calorieCalculator = new CalorieCalculator();
                    calorieCalculator.countCalories(age, height, currentWeight, targetWeight, gender, activity);
                    double caloriesNeeded = calorieCalculator.caloriesNeededPerDay;

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Enter name for your diet:");

                    // Set up the input
                    final EditText input = new EditText(context);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String dietName = input.getText().toString().trim();
                            GenerateMealAsync generator = new GenerateMealAsync(manager,calorieCalculator, menuViewModel, dietName);
                            generator.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR ,String.valueOf(caloriesNeeded), String.valueOf(currentWeight), String.valueOf(targetWeight));
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Create and show the dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Warning");
                    if(bmiCalculator.type == "underweight") {
                        builder.setMessage("You're underweight. Not recommend to lose weight.");
                    } else if (bmiCalculator.type == "overweight") {
                        builder.setMessage("You're overweight. Not recommend to gain weight");
                    }

                    builder.setPositiveButton("OK", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

}