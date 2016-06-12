package com.innocept.taximastercustomer.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.ui.activity.CurrentOrderActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Dulaj on 08-Mar-16.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    private List<Order> dataSet;

    public MyOrderAdapter(Context context, List<Order> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutOrderListItem;
        TextView textFromTo;
        TextView textTime;
        TextView textDriverName;
        TextView textDriverPhone;
        TextView textState;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutOrderListItem = (LinearLayout) itemView.findViewById(R.id.linear_taxi_list_item);
            textFromTo = (TextView) itemView.findViewById(R.id.text_my_orderfrom_to);
            textTime = (TextView) itemView.findViewById(R.id.text_my_order_time);
            textDriverName = (TextView) itemView.findViewById(R.id.text_my_order_name);
            textDriverPhone = (TextView) itemView.findViewById(R.id.text_my_order_phone);
            textState = (TextView) itemView.findViewById(R.id.text_my_order_state);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_on_going_order_list, parent, false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 3, 0, 3);
        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textFromTo.setText(dataSet.get(position).getOrigin() + " to " + dataSet.get(position).getDestination());
        viewHolder.textTime.setText(new SimpleDateFormat("yyyy-MM-dd HH-mm").format(dataSet.get(position).getTime()));
        viewHolder.textDriverName.setText(dataSet.get(position).getDriver().getFirstName());
        viewHolder.textDriverPhone.setText(dataSet.get(position).getDriver().getPhone());

        Order.OrderState orderState = dataSet.get(position).getOrderState();
        if (orderState == Order.OrderState.PENDING) {
            viewHolder.textState.setTextColor(Color.BLUE);
        } else if (orderState == Order.OrderState.ACCEPTED) {
            viewHolder.textState.setTextColor(Color.GREEN);
        } else if (orderState == Order.OrderState.REJECTED) {
            viewHolder.textState.setTextColor(Color.RED);
        } else if (orderState == Order.OrderState.NOW) {
            viewHolder.textState.setTextColor(Color.YELLOW);
        }
        viewHolder.textState.setText(orderState.toString());

        viewHolder.linearLayoutOrderListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSet.get(position).getOrderState() == Order.OrderState.ACCEPTED) {

                }
                if (dataSet.get(position).getOrderState() == Order.OrderState.NOW) {
                    Intent intent = new Intent(context, CurrentOrderActivity.class);
                    intent.putExtra("order", dataSet.get(position));
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
