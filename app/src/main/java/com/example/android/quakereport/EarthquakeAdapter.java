package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wen-zhao on 9/9/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes ){
        super(context, 0, earthquakes);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthqauke_list_item, parent, false);
        }
        //get the current object from the position
        Earthquake currentEarthquake = getItem(position);
        //initialize all three text views
        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        TextView city = (TextView) listItemView.findViewById(R.id.location_city);
        TextView primary = (TextView) listItemView.findViewById(R.id.primary_location);
        TextView date_textview = (TextView) listItemView.findViewById(R.id.date);
        TextView time_textview = (TextView) listItemView.findViewById(R.id.time);
        double magnitudeValue = currentEarthquake.getmMagnitude();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String magOfString = decimalFormat.format(magnitudeValue);
        magnitude.setText(magOfString);
        //split place info into two strings and set to the 2 text views
        String placeInfo = currentEarthquake.getlLocationCity();
        if (placeInfo.contains("of")){
            int indexOfOf = placeInfo.indexOf("of");
            String offsets = placeInfo.substring(0,indexOfOf+2);
            String primaryCity = placeInfo.substring(indexOfOf+2,placeInfo.length());
            city.setText(offsets);
            primary.setText(primaryCity);

        }else{
            city.setText("Near the");
            primary.setText(placeInfo);
        }
       //format unix time to readable time
        Date dateObject  = new Date(currentEarthquake.getmDateMillesecondes());
        date_textview.setText(formatDate(dateObject));
        time_textview.setText(formatTime(dateObject));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }
    public String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateOfString =  dateFormat.format(dateObject);
        return dateOfString;
    }
    public String formatTime(Date dateOject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String timeOfString =dateFormat.format(dateOject);
        return timeOfString;
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
