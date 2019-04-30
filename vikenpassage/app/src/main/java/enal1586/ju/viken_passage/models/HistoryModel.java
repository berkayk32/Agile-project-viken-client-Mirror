package enal1586.ju.viken_passage.models;

    public class HistoryModel {

        String payment;
        String date;

        public HistoryModel (String Payment, String Date) {
            this.payment=Payment;
            this.date=Date;

        }

        public String getPayment() {
            return payment;
        }

        public String getDate() {
            return date;
        }

}


