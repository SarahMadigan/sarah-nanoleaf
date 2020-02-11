package com.nanoleaf.sarah.sarahnanoleafinterview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

        iTextInput = findViewById(R.id.i_input);
        jTextInput = findViewById(R.id.j_input);
        kTextInput = findViewById(R.id.k_input);
        mTextInput = findViewById(R.id.m_input);
        nTextInput = findViewById(R.id.n_input);
        submitButton = findViewById(R.id.submit_button);
        progressBar = findViewById(R.id.progressBar_cyclic);
        progressBar.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    numColours = Integer.parseInt(jTextInput.getText().toString());
                    numEachColour = Integer.parseInt(kTextInput.getText().toString());
                    totalLightbulbs = Integer.parseInt(iTextInput.getText().toString());
                    if (numColours * numEachColour != totalLightbulbs) {
                        // Error
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Error")
                                .setMessage("Total number of colours doesn't match")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else if (numToPick > totalLightbulbs) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Error")
                                .setMessage("Number to pick is bigger than total")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    } else {
                        numToPick = Integer.parseInt(mTextInput.getText().toString());
                        numSimulationRuns = Integer.parseInt(mTextInput.getText().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runSimulation(numColours, numEachColour, totalLightbulbs, numToPick, numSimulationRuns);
                            }
                        }).start();
                        Log.e("Main", "After run simulation");
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                        progressBar.setMax(10);
                        progressBar.setProgress(1);
                        submitButton.setClickable(false);
                        Log.e("Main", "After progress");
                    }
                }
            }
        });
    }


    float runSimulation(int numColours, int numEachColour, int totalLightbulbs, int numToPick, int numSimulationRuns) {
        Log.e("Main", "runsimulation");
        Log.e("Main", "Run delay");

        Log.e("Main", "Done delay");

        int total = 0;
        Set<Integer> currentColours = new HashSet<>();

        for (int i = 0; i < numSimulationRuns; i++) {
            for (int j = 0; j < numToPick; j++) {
                Random r = new Random();
                int randomNum = r.nextInt(totalLightbulbs);
                int colourIndex = (int) randomNum/numEachColour;
                currentColours.add(colourIndex);
                Log.e("Colour index", "" + colourIndex);
            }
            total += currentColours.size();
            Log.e("Colour size", "" + currentColours.size());
            currentColours.clear();
        }

        final float average = (float) total / (float) numSimulationRuns;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(10);
                progressBar.setVisibility(View.GONE);
                submitButton.setClickable(true);

                String answer = String.format("%.2f", average);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Success")
                        .setMessage("The average is: " + answer)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });

        return 0;
    }


}
