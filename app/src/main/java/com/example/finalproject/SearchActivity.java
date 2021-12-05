package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button displayResultsButton;
    EditText searchLatitudeView;
    EditText searchLongitudeView;
    EditText searchDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        displayResultsButton = findViewById(R.id.displayResultsButton);
        displayResultsButton.setOnClickListener(bt -> executeSearch());

        Toolbar toolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void executeSearch() {
        Bundle dataToPass = new Bundle();

        Intent goToDisplayResultsActivity = new Intent(this, DisplayResultsActivity.class);
        searchLatitudeView = findViewById(R.id.searchLatitude);
        searchLongitudeView = findViewById(R.id.searchLongitude);
        searchDateView = findViewById(R.id.searchDate);

        dataToPass.putString("latitude", searchLatitudeView.getText().toString());
        dataToPass.putString("longitude", searchLongitudeView.getText().toString());
        dataToPass.putString("date", searchDateView.getText().toString());
        goToDisplayResultsActivity.putExtras(dataToPass);

        startActivity(goToDisplayResultsActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = "";

        switch(item.getItemId()) {
            case R.id.helpMenuItem:
                // Create a message for the user
                message = "Enter the coordinates of the location you are looking for and the date" +
                        " the photo was taken, then hit search to view the image.";
                break;
        }
        // Write the message to an AlertDialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent mainActivity = new Intent(this, MainActivity.class);
        Intent searchActivity = new Intent(this, SearchActivity.class);
        Intent favouritesActivity = new Intent(this, FavouritesActivity.class);

        switch(item.getItemId()) {
            case R.id.nav_main:
                startActivity(mainActivity);
                break;
            case R.id.nav_search:
                startActivity(searchActivity);
                break;
            case R.id.nav_favourites:
                startActivity(favouritesActivity);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}