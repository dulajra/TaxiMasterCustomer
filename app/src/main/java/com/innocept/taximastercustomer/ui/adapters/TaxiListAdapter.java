package com.innocept.taximastercustomer.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.Time;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class TaxiListAdapter extends RecyclerView.Adapter<TaxiListAdapter.ViewHolder> {

    private List<Taxi> dataSet;
    Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private int yr, month, day, hour, min;
    private Calendar calendar;

    public TaxiListAdapter(Context context, List<Taxi> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
        this.calendar = Calendar.getInstance();
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
                final TextInputLayout textInputLayoutContact = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_contact);
                final EditText editTextContact = (EditText) alertDialogView.findViewById(R.id.text_contact);
                final Spinner spinnerDate = (Spinner) alertDialogView.findViewById(R.id.spinner_date);
                final TextInputLayout textInputLayoutTime = (TextInputLayout) alertDialogView.findViewById(R.id.input_layout_time);
                final EditText editTextTime = (EditText) alertDialogView.findViewById(R.id.edit_time);
                final EditText editTextNote = (EditText) alertDialogView.findViewById(R.id.text_note);
//                final Spinner spinnerTime = (Spinner) alertDialogView.findViewById(R.id.spinner_time);

                editTextFrom.setText(((NewOrderActivity) context).textViewFrom.getText().toString());
                editTextTo.setText(((NewOrderActivity) context).textViewTo.getText().toString());
                editTextContact.setText(ApplicationPreferences.getUser().getPhone());

                final ArrayList<String> spinnerDateArray = new ArrayList<String>();
                spinnerDateArray.add("ASAP");
                spinnerDateArray.add("Today");
                spinnerDateArray.add("Tomorrow");
                spinnerDateArray.add("Pick a date...");
                final ArrayAdapter<String> spinnerDateArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerDateArray);
                spinnerDate.setAdapter(spinnerDateArrayAdapter);
                spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, dataSet.get(position).getDurationValue());
                                textInputLayoutTime.setVisibility(View.GONE);
                                break;
                            case 1:
                                calendar = Calendar.getInstance();
                                textInputLayoutTime.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                calendar = Calendar.getInstance();
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                textInputLayoutTime.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                DatePickerDialog datePickerDialog = new DatePickerDialog();
                                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                                        calendar.set(Calendar.YEAR, year);
                                        calendar.set(Calendar.MONTH, monthOfYear);
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                        spinnerDateArray.set(2, new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
                                        spinnerDateArrayAdapter.notifyDataSetChanged();
                                    }
                                });
                                datePickerDialog.show(((Activity) context).getFragmentManager(), "TIME");
                                textInputLayoutTime.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                editTextTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog();
                        Calendar c = Calendar.getInstance();
                        timePickerDialog.setStartTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                        timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                editTextTime.setText(new SimpleDateFormat("hh:mm a").format(calendar.getTime()));
                            }
                        });
                        timePickerDialog.show(((Activity) context).getFragmentManager(), "TIME");
                    }
                });

                final AlertDialog alertDialog;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(alertDialogView);
                alertDialogBuilder.setTitle("Place new order");
                alertDialogBuilder.setPositiveButton("Ok", null);
                alertDialogBuilder.setNegativeButton("Cancel", null);
                alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isSuccess;
                                if (editTextContact.getText().toString().trim().length() < 9) {
                                    textInputLayoutContact.setError("Invalid phone number");
                                    requestFocus(editTextContact);
                                    isSuccess = false;
                                } else {
                                    isSuccess = true;
                                    textInputLayoutContact.setErrorEnabled(false);
                                }

                                if (textInputLayoutTime.getVisibility() == View.VISIBLE) {
                                    if (editTextTime.getText().toString().trim().isEmpty()) {
                                        textInputLayoutTime.setError("Time cannot be empty");
                                        requestFocus(editTextTime);
                                        isSuccess = false;
                                    } else {
                                        isSuccess = true;
                                        textInputLayoutTime.setErrorEnabled(false);
                                    }
                                }

                                if (isSuccess) {
                                    ((NewOrderActivity) context).placeOrder(new Time(calendar.getTime()), editTextNote.getText().toString(), dataSet.get(position).getDriver(), editTextContact.getText().toString());
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
