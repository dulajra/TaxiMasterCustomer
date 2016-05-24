package com.innocept.taximastercustomer.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.ui.activity.PlaceOrderActivity;

import java.util.List;

/**
 * Created by Dulaj on 30-Dec-15.
 */
public class TaxiListAdapter extends RecyclerView.Adapter<TaxiListAdapter.ViewHolder> {

    private List<Taxi> dataSet;
    Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public TaxiListAdapter(Context context, List<Taxi> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linearLayout;
        public TextView textViewModel;
        public TextView textViewDistance;
        public TextView textViewDuration;
        public TextView textViewName;
        public TextView textViewNoOfSeats;

        public ViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout)v.findViewById(R.id.linear_taxi_list_item);
            textViewModel = (TextView) v.findViewById(R.id.text_model);
            textViewName = (TextView) v.findViewById(R.id.text_name);
            textViewDistance = (TextView) v.findViewById(R.id.text_distance);
            textViewDuration = (TextView) v.findViewById(R.id.text_duration);
            textViewNoOfSeats = (TextView) v.findViewById(R.id.text_no_of_seats);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_taxi_list, parent, false);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,3,0,3);
        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textViewModel.setText(dataSet.get(position).getModel());
        viewHolder.textViewDistance.setText(dataSet.get(position).getDistance());
        viewHolder.textViewDuration.setText(dataSet.get(position).getDuration());
        viewHolder.textViewName.setText(dataSet.get(position).getDriver().getFirstName() + " " + dataSet.get(position).getDriver().getLastName());
        viewHolder.textViewNoOfSeats.setText(dataSet.get(position).getNoOfSeats() + " Passengers");

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((Activity)context).startActivity(new Intent(context, PlaceOrderActivity.class));

//                LayoutInflater inflater = LayoutInflater.from(context);
//                View alertDialogView = inflater.inflate(R.layout.inflater_alert_dialog_place_order, null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setView(alertDialogView);
//                alertDialogBuilder.create().show();

                TimePickerDialog timePickerDialog = new TimePickerDialog();
                timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                    }
                });

                timePickerDialog.show(((Activity)context).getFragmentManager(), "TIME");

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
