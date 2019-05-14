package enal1586.ju.viken_passage.models;

import android.location.Geocoder;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class HistoryModel {

    String _payment;
    Date _date;
    GeoPoint _geopoint;

    public HistoryModel(Date Date, String Payment,GeoPoint geoPoint) {
        this._payment = Payment;
        this._date = Date;
        this._geopoint = geoPoint;

    }

    public String getPayment() {
        return _payment;
    }

    public Date getDate() {
        return _date;
    }

    public GeoPoint getGeopoint() {return _geopoint; }

}


