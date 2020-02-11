package com.nanoleaf.sarah.sarahnanoleafinterview;

import android.app.IntentService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText iTextInput;
    EditText jTextInput;
    EditText kTextInput;
    EditText mTextInput;
    EditText nTextInput;
    Button submitButton;

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numColours = Integer.parseInt(jTextInput.getText().toString());
                numEachColour = Integer.parseInt(kTextInput.getText().toString());
                totalLightbulbs = Integer.parseInt(iTextInput.getText().toString());
                if (numColours * numEachColour != totalLightbulbs) {
                    // Error
                }
            }
        });
    }


}
