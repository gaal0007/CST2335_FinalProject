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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Favourite> favourites = new ArrayList<>();
    SQLiteDatabase db;
    ApplicationDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ApplicationDao dao = new ApplicationDao(this);
        db = dao.getWritableDatabase();

        loadFavouritesFromDatabase();

        ListAdapter favouritesListAdapter = new ListAdapter();

        ListView favouritesListView = findViewById(R.id.favouritesListView);
        favouritesListView.setAdapter(favouritesListAdapter);

        favouritesListView.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Delete")
                .setMessage("Remove from Favourites?")
                .setPositiveButton("Yes", (click, arg) -> {
                    favourites.remove(pos);
                    favouritesListAdapter.notifyDataSetChanged();
                    db.delete(dao.TABLE_NAME, dao.COL_ID + "= ?", new String[] {Long.toString(id)});
                    Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (click, arg) -> { })
                .create().show();
            return true;
        });

        favouritesListView.setOnItemClickListener((p, b, pos, id) -> {
            Snackbar.make(favouritesListView, "Latitude: " + favourites.get(pos).latitude
                    + " Longitude: " + favourites.get(pos).longitude
                    + " Date: " + favourites.get(pos).date, Snackbar.LENGTH_LONG).show();
        });

        Toolbar toolbar = findViewById(R.id.favouritesToolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Retrieves favourites data from the local db and adds it to the ListView
     *
     * @author Andrew
     * @since 1.0
     */
    private void loadFavouritesFromDatabase() {
        // Get the database


        // Query the db
        String[] columns = {dao.COL_ID, dao.COL_LATITUDE, dao.COL_LONGITUDE, dao.COL_DATE, dao.COL_IMAGE};
        Cursor results = db.query(dao.TABLE_NAME, columns, null, null, null, null, null);

        int latitudeColumnIndex = results.getColumnIndex(dao.COL_LATITUDE);
        int longitudeColumnIndex = results.getColumnIndex(dao.COL_LONGITUDE);
        int dateColumnIndex = results.getColumnIndex(dao.COL_DATE);
        int imageColumnIndex = results.getColumnIndex(dao.COL_IMAGE);
        int idColumnIndex = results.getColumnIndex(dao.COL_ID);

        // Iterate over the results and add each result to the favourites array
        while(results.moveToNext()) {
            Favourite myFavourite = new Favourite();
            myFavourite.favouriteId = results.getString(idColumnIndex);
            myFavourite.latitude = results.getString(latitudeColumnIndex);
            myFavourite.longitude = results.getString(longitudeColumnIndex);
            myFavourite.date = results.getString(dateColumnIndex);
            myFavourite.image = retrieveBitmapFromFileName(results.getString(imageColumnIndex));
            favourites.add(myFavourite);
        }
    }

    private Bitmap retrieveBitmapFromFileName(String fileName) {
        String directory = new File(getFilesDir(), fileName).getAbsolutePath();
        Bitmap myBitmap =  BitmapFactory.decodeFile(directory);
        return myBitmap;
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favourites.size();
        }

        @Override
        public Object getItem(int position) {
            return "this is row " + position;
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(favourites.get(position).favouriteId);
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.layout_favourite, parent, false);

            ImageView image = newView.findViewById(R.id.favouriteImage);

            Favourite myFavourite = favourites.get(position);
            image.setImageBitmap(myFavourite.image);

            return newView;
        }
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
                message = "Tap an image to view it's coordinates and date or long press to delete.";
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