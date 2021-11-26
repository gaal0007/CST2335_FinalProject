package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button searchActivityButton;
    Button favouritesActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent goToSearchActivity = new Intent(this, SearchActivity.class);
        Intent goToFavouritesActivity = new Intent(this, FavouritesActivity.class);

        searchActivityButton = findViewById(R.id.searchActivityButton);
        favouritesActivityButton = findViewById(R.id.favouritesActivityButton);

        searchActivityButton.setOnClickListener(bt -> startActivity(goToSearchActivity));
        favouritesActivityButton.setOnClickListener(bt -> startActivity(goToFavouritesActivity));

    }
}