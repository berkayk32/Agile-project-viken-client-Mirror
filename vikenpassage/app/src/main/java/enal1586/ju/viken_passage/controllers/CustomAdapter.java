package enal1586.ju.viken_passage.controllers;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import enal1586.ju.viken_passage.R;
import enal1586.ju.viken_passage.models.HistoryModel;

public class CustomAdapter extends ArrayAdapter<HistoryModel> implements View.OnClickListener{

    private ArrayList<HistoryModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtPayment;
        TextView txtDate;
    }

    public CustomAdapter(ArrayList<HistoryModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        HistoryModel historyModel=(HistoryModel)object;

    }

    private int lastPosition = -1;

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
            viewHolder.txtPayment = (TextView) convertView.findViewById(R.id.Payment);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.Timestamp);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtPayment.setText(historyModel.getPayment());
        viewHolder.txtDate.setText(historyModel.getDate());
        // Return the completed view to render on screen
        return convertView;
    }
}
