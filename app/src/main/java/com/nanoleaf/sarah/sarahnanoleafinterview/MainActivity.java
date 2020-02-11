package com.nanoleaf.sarah.sarahnanoleafinterview;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText iTextInput;
    EditText jTextInput;
    EditText kTextInput;
    EditText mTextInput;
    EditText nTextInput;
    Button submitButton;

    ProgressBar progressBar;

    int numColours, totalLightbulbs, numEachColour, numToPick, numSimulationRuns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Find all the views
        iTextInput = findViewById(R.id.i_input);
        jTextInput = findViewById(R.id.j_input);
        kTextInput = findViewById(R.id.k_input);
        mTextInput = findViewById(R.id.m_input);
        nTextInput = findViewById(R.id.n_input);
        submitButton = findViewById(R.id.submit_button);
        progressBar = findViewById(R.id.progressBar_cyclic);
        //Hide the spinner, nothing is in progress yet
        progressBar.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check that all the fields have been filled
                if (jTextInput.getText().toString().isEmpty() ||
                kTextInput.getText().toString().isEmpty() ||
                iTextInput.getText().toString().isEmpty() ||
                mTextInput.getText().toString().isEmpty() ||
                nTextInput.getText().toString().isEmpty() ) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Error")
                            .setMessage("Please fill out all the fields")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {

                    // Get the values from all the fields
                    numColours = Integer.parseInt(jTextInput.getText().toString());
                    numEachColour = Integer.parseInt(kTextInput.getText().toString());
                    totalLightbulbs = Integer.parseInt(iTextInput.getText().toString());
                    numToPick = Integer.parseInt(mTextInput.getText().toString());
                    numSimulationRuns = Integer.parseInt(mTextInput.getText().toString());

                    // Check that total number matches the number of balls and number of colours
                    if (numColours * numEachColour != totalLightbulbs) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Error")
                                .setMessage("Total number of colours doesn't match")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else if (numToPick > totalLightbulbs) {
                        // Check that the number is to pick is less than the number of lightbulbs
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Error")
                                .setMessage("Number to pick is bigger than total")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Start running the simulation on another thread
                                runSimulation(numEachColour, totalLightbulbs, numToPick, numSimulationRuns);
                            }
                        }).start();

                        // Set up progress bar
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                        progressBar.setMax(10);
                        progressBar.setProgress(1);

                        // Disable submit button so they can't click it twice
                        submitButton.setClickable(false);
                    }
                }
            }
        });
    }


    void runSimulation(int numEachColour, int totalLightbulbs, int numToPick, int numSimulationRuns) {

        int total = 0;
        // Hash set that will enforce uniqueness on the colours
        Set<Integer> currentColours = new HashSet<>();

        // For each run of the simulation....
        for (int i = 0; i < numSimulationRuns; i++) {
            // For each pull out of the bag....
            for (int j = 0; j < numToPick; j++) {
                Random r = new Random();
                int randomNum = r.nextInt(totalLightbulbs);
                int colourIndex = (int) randomNum/numEachColour;
                currentColours.add(colourIndex);
            }
            // Add number of colours to the total
            total += currentColours.size();
            currentColours.clear();
        }

        // Calculate the average with decimals
        final float average = (float) total / (float) numSimulationRuns;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Hide the spinner
                progressBar.setProgress(10);
                progressBar.setVisibility(View.GONE);
                submitButton.setClickable(true);

                // Format the string to 2 decimal places
                String answer = String.format("%.2f", average);

                // Show the answer dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Success")
                        .setMessage("The average is: " + answer)
                        .setPositiveButton(android.R.string.ok, null)
                        .setCancelable(false)
                        .show();
            }
        });

        return;
    }


}
