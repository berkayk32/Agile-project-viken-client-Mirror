package enal1586.ju.viken_passage.models;

import java.util.Date;

public class HistoryModel {

    String _payment;
    Date _date;

    public HistoryModel(Date Date, String Payment) {
        this._payment = Payment;
        this._date = Date;

    }

    public String getPayment() {
        return _payment;
    }

    public Date getDate() {
        return _date;
    }

}


