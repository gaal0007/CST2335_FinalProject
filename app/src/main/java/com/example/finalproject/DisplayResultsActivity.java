package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class DisplayResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Bundle searchData;
    ProgressBar progressBar = null;
    ImageView resultImageView;
    TextView resultLatitudeView;
    TextView resultLongitudeView;
    TextView resultDateView;
    Button addToFavouritesButton;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        searchData = getIntent().getExtras();

        resultImageView = findViewById(R.id.resultImage);
        resultLatitudeView = findViewById(R.id.resultLatitude);
        resultLongitudeView = findViewById(R.id.resultLongitude);
        resultDateView = findViewById(R.id.resultDate);
        addToFavouritesButton = findViewById(R.id.addToFavouritesButton);
        addToFavouritesButton.setOnClickListener(btn -> addToFavourites());
        imageName = "";

        Toolbar toolbar = findViewById(R.id.resultsToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HTTPRequest req = new HTTPRequest();
        req.execute("https://api.nasa.gov/planetary/earth/imagery?lon=" + searchData.getString("longitude")
                + "&lat=" + searchData.getString("latitude") + "&date=" + searchData.getString("date")
                + "&api_key=DEMO_KEY");
    }

    private class HTTPRequest extends AsyncTask<String, Integer, String> {

        Bitmap resultImageBitmap;
        String resultLatitude;
        String resultLongitude;
        String resultDate;

        @Override
        protected String doInBackground(String... args) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                resultImageBitmap = fetchBitmapFromUrl(url);
                publishProgress(25);

                resultLatitude = searchData.getString("latitude");
                publishProgress(50);

                resultLongitude = searchData.getString("longitude");
                publishProgress(75);

                resultDate = searchData.getString("date");
                publishProgress(100);

            } catch (IOException e) {
                Log.e("Error", e.getMessage());
            }

            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            resultImageView.setImageBitmap(resultImageBitmap);
            resultLongitudeView.setText(resultLongitude);
            resultLatitudeView.setText(resultLatitude);
            resultDateView.setText(resultDate);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Fetches an image from the supplied URL string and saves it to the device.
     *
     * @author Andrew
     * @since 1.0
     */
    public Bitmap fetchBitmapFromUrl (URL url) throws IOException {
        Bitmap bitmap = null;
        imageName = generateUUIDString();
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        int responseCode = connection.getResponseCode();
        if(responseCode == 200)
        {
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        }

        FileOutputStream outputStream = openFileOutput(imageName, MODE_PRIVATE);
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        outputStream.flush();
        outputStream.close();

        return bitmap;
    }

    /**
     * Generates a unique ID and returns it as a String.
     *
     * @author Andrew
     * @since 1.0
     */
    public String generateUUIDString() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    /**
     * Adds the current search result to a local db of favourites.
     *
     * @author Andrew
     * @since 1.0
     */
    public void addToFavourites() {
        //initialize the db and the access object
        ApplicationDao dao = new ApplicationDao(this);
        SQLiteDatabase db = dao.getWritableDatabase();

        String latitude = resultLatitudeView.getText().toString();
        String longitude = resultLongitudeView.getText().toString();
        String date = resultDateView.getText().toString();
        String image = imageName;

        //create a ContentValues object and add the search result to it
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(dao.COL_LATITUDE, latitude);
        newRowValues.put(dao.COL_LONGITUDE, longitude);
        newRowValues.put(dao.COL_DATE, date);
        newRowValues.put(dao.COL_IMAGE, image);

        //insert the new row into the database
        db.insert(dao.TABLE_NAME, null, newRowValues);

        //tell the user that the new favourite has been added
        Toast.makeText(this, "Added to Favourites!", Toast.LENGTH_SHORT).show();
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
                message = "Tap the 'Add to Favourites' button to save this image to your favourites.";
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