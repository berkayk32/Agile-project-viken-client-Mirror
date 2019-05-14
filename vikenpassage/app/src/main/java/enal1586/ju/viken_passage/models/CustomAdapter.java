package enal1586.ju.viken_passage.models;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import enal1586.ju.viken_passage.R;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class CustomAdapter extends ArrayAdapter<HistoryModel> implements View.OnClickListener{

    private ArrayList<HistoryModel> historyDataSet;
    Context mContext;

    private int lastPosition = -1;

    public CustomAdapter(ArrayList<HistoryModel> historyData, Context context) {
        super(context, R.layout.row_item, historyData);
        this.historyDataSet = historyData;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        if (getItem(position) instanceof HistoryModel) {
            HistoryModel historyModel = getItem(position);
        } else {
            makeText(v.getContext(), "onClick failed, getItem is not of type", LENGTH_SHORT).show();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HistoryModel historyModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.textPayment = (TextView) convertView.findViewById(R.id.Payment);
            viewHolder.textDate = (TextView) convertView.findViewById(R.id.Timestamp);

            viewHolder.textLocation = (TextView) convertView.findViewById(R.id.Location);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.textPayment.setText(historyModel.getPayment());
        viewHolder.textDate.setText(historyModel.getDate().toString());
        viewHolder.textLocation.setText(historyModel.getGeopoint().toString());
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView textPayment;
        TextView textDate;
        TextView textLocation;
    }
}
