package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SearchActivity extends AppCompatActivity {

    Button displayResultsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent goToDisplayResultsActivity = new Intent(this, DisplayResultsActivity.class);

        displayResultsButton.findViewById(R.id.displayResultsButton);
        displayResultsButton.setOnClickListener(bt -> startActivity(goToDisplayResultsActivity));
    }
}