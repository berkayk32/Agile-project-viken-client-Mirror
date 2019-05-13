package enal1586.ju.viken_passage.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import enal1586.ju.viken_passage.R;

public class GPS extends AppCompatActivity {
    String names[] = {"Taj mahal","Eiffel Tower","Burj khalifa","Jönköping"};
    Double latitude[] = {27.1750,48.8584,25.1972,57.777942};
    Double longitude[] = {78.0422,2.2945,55.2744,14.158095};
    private TextView textView, tvCountry, tvCity, tvState, tvPincode, tvFeature;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        spinner = findViewById(R.id.spinner);

        textView = findViewById(R.id.tv);
        tvCountry = findViewById(R.id.tvCountry);
        tvState = findViewById(R.id.tvState);
        tvCity = findViewById(R.id.tvCity);
        tvPincode = findViewById(R.id.tvPincode);
        tvFeature = findViewById(R.id.tvFeature);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, names);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTexts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setTexts(int position){

        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude[position], longitude[position], 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("max"," "+addresses.get(0).getMaxAddressLineIndex());

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        addresses.get(0).getAdminArea();
        textView.setText(address);
        tvFeature.setText(knownName);
        tvCountry.setText(country);
        tvState.setText(state);
        tvCity.setText(city);
        tvPincode.setText(postalCode);

    }
}
