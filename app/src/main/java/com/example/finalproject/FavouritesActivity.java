package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class FavouritesActivity extends AppCompatActivity {

    private ArrayList<Favourite> favourites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //----Setting up a demo Favourite object----
        Favourite demoFavourite = new Favourite();
        demoFavourite.favouriteId = 1;
        demoFavourite.image = drawableToBitmap(getDrawable(R.drawable.imagery_demo));
        demoFavourite.latitude = "Latitude: 1.5";
        demoFavourite.longitude = "Longitude: 100.75";
        demoFavourite.date = "Date: 2014-02-01";
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
            TextView latitude = newView.findViewById(R.id.favouriteLatitude);
            TextView longitude = newView.findViewById(R.id.favouriteLongitude);
            TextView date = newView.findViewById(R.id.favouriteDate);

            Favourite myFavourite = favourites.get(position);
            image.setImageBitmap(myFavourite.image);
            latitude.setText(myFavourite.latitude);
            longitude.setText(myFavourite.longitude);
            date.setText(myFavourite.date);

            return newView;
        }
    }

    // TODO: Get rid of this when you don't need it anymore
    // Yes, this is from StackOverflow. No, it won't be in the final. Just need it for a demo
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

    private class Favourite {
        double favouriteId;
        String latitude;
        String longitude;
        String date;
        Bitmap image;
    }
}