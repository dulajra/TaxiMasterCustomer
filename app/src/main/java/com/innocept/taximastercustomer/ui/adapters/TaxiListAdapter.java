package com.innocept.taximastercustomer.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;

import java.util.ArrayList;
import java.util.Calendar;
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

    private int yr, month, day, hour, min;

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
            linearLayout = (LinearLayout) v.findViewById(R.id.linear_taxi_list_item);
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
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 3, 0, 3);
        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        String[] time = dataSet.get(position).getDuration().split(" ");
        viewHolder.textViewModel.setText(dataSet.get(position).getModel());
        viewHolder.textViewDistance.setText(dataSet.get(position).getDistance());
        viewHolder.textViewDuration.setText(time[0] + '\n' + time[1]);
        viewHolder.textViewName.setText(dataSet.get(position).getDriver().getFirstName());
        viewHolder.textViewNoOfSeats.setText(dataSet.get(position).getNoOfSeats() + " Passengers");

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(context);
                View alertDialogView = inflater.inflate(R.layout.inflater_alert_dialog_place_order, null);

                final EditText editTextFrom = (EditText) alertDialogView.findViewById(R.id.edit_from);
                final EditText editTextTo = (EditText) alertDialogView.findViewById(R.id.edit_to);
                final EditText editTextNote = (EditText) alertDialogView.findViewById(R.id.text_note);
                final EditText editTextContact = (EditText) alertDialogView.findViewById(R.id.text_contact);

                editTextFrom.setText(((NewOrderActivity)context).textViewFrom.getText().toString());
                editTextTo.setText(((NewOrderActivity)context).textViewTo.getText().toString());

                final ArrayList<String> spinnerDateArray = new ArrayList<String>();
                spinnerDateArray.add("Today");
                spinnerDateArray.add("Tomorrow");
                spinnerDateArray.add("Pick a date...");
                final Spinner spinnerDate = (Spinner) alertDialogView.findViewById(R.id.spinner_date);
                ArrayAdapter<String> spinnerTimeArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerDateArray);
                spinnerDate.setAdapter(spinnerTimeArrayAdapter);
                spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                DatePickerDialog datePickerDialog = new DatePickerDialog();
                                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                                        yr = year;
                                        month = monthOfYear;
                                        day = dayOfMonth;

                                        spinnerDateArray.set(2, dayOfMonth + "-" + monthOfYear + "-" + year);
                                    }
                                });

                                datePickerDialog.show(((Activity) context).getFragmentManager(), "TIME");
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final ArrayList<String> spinnerTimeArray = new ArrayList<String>();
                spinnerTimeArray.add("ASAP");
                spinnerTimeArray.add("Pick a time...");
                Spinner spinnerTime = (Spinner) alertDialogView.findViewById(R.id.spinner_time);
                ArrayAdapter<String> spinnerDateArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerTimeArray);
                spinnerTime.setAdapter(spinnerDateArrayAdapter);
                spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                TimePickerDialog timePickerDialog = new TimePickerDialog();
                                timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                        hour = hourOfDay;
                                        min = minute;

                                        spinnerTimeArray.set(1, hourOfDay + ":" + minute);
                                    }
                                });
                                timePickerDialog.show(((Activity) context).getFragmentManager(), "TIME");
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(alertDialogView);
                alertDialogBuilder.setTitle("Place new order");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, yr);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, min);

                        String note = editTextNote.getText().toString();
                        ((NewOrderActivity)context).placeOrder(calendar.getTime(), note, dataSet.get(position).getDriver(),editTextContact.getText().toString());
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", null);
                alertDialogBuilder.create().show();
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
