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
import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.Date;

public class FavouritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Favourite> favourites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //----Setting up a demo Favourite object----
        Favourite demoFavourite = new Favourite();
        demoFavourite.favouriteId = 1;
        demoFavourite.image = drawableToBitmap(getDrawable(R.drawable.imagery_demo));
        demoFavourite.latitude = "1.5";
        demoFavourite.longitude = "100.75";
        demoFavourite.date = "2014-02-01";
        favourites.add(demoFavourite);
        favourites.add(demoFavourite);
        favourites.add(demoFavourite);
        //------------------------------------------

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

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return favourites.size();
        }

        @Override
        public Object getItem(int position) {
            return "this is row " + position;
        }

        // TODO: Position here should be the favourite's id from the database
        @Override
        public long getItemId(int position) {
            return (long) position;
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

    // TODO: Get rid of this when you don't need it anymore
    // Yes, this is from StackOverflow. No, it won't be in the final. Just need it for testing purposes
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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

    private class Favourite {
        double favouriteId;
        String latitude;
        String longitude;
        String date;
        Bitmap image;
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