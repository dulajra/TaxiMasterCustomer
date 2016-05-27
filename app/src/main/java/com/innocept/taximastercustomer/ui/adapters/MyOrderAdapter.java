package com.innocept.taximastercustomer.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Order;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Dulaj on 08-Mar-16.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{

    Context context;
    private List<Order> orderList;

    public MyOrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textFromTo;
        TextView textTime;
        TextView textDriverName;
        TextView textDriverPhone;
        TextView textState;

        public ViewHolder(View itemView) {
            super(itemView);

            textFromTo = (TextView) itemView.findViewById(R.id.text_my_orderfrom_to);
            textTime = (TextView) itemView.findViewById(R.id.text_my_order_time);
            textDriverName = (TextView)itemView.findViewById(R.id.text_my_order_name);
            textDriverPhone = (TextView)itemView.findViewById(R.id.text_my_order_phone);
            textState = (TextView)itemView.findViewById(R.id.text_my_order_state);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_on_going_order_list, parent, false);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,3,0,3);
        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textFromTo.setText(orderList.get(position).getOrigin() + " to " + orderList.get(position).getDestination());
        viewHolder.textTime.setText(new SimpleDateFormat("yyyy-MM-dd HH-mm").format(orderList.get(position).getTime()));
        viewHolder.textDriverName.setText(orderList.get(position).getDriver().getFirstName());
        viewHolder.textDriverPhone.setText(orderList.get(position).getDriver().getPhone());

        Order.OrderState orderState = orderList.get(position).getOrderState();
        if(orderState == Order.OrderState.PENDING){
            viewHolder.textState.setTextColor(Color.BLUE);
        }
        else if(orderState == Order.OrderState.ACCEPTED){
            viewHolder.textState.setTextColor(Color.GREEN);
        }
        else if(orderState == Order.OrderState.REJECTED){
            viewHolder.textState.setTextColor(Color.RED);
        }
        viewHolder.textState.setText(orderState.toString());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
