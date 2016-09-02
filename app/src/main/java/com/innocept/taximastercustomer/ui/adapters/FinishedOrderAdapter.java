package com.innocept.taximastercustomer.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.FinishedOrder;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.ui.activity.CurrentOrderActivity;
import com.innocept.taximastercustomer.ui.activity.FinishedOrderActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Dulaj on 08-Mar-16.
 */
public class FinishedOrderAdapter extends RecyclerView.Adapter<FinishedOrderAdapter.ViewHolder> {

    Context context;
    private List<FinishedOrder> dataSet;

    public FinishedOrderAdapter(Context context, List<FinishedOrder> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutOrderListItem;
        TextView textFromTo;
        TextView textTime;
        ImageView imageView;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutOrderListItem = (LinearLayout) itemView.findViewById(R.id.linear_taxi_list_item);
            textFromTo = (TextView) itemView.findViewById(R.id.text_my_order_from_to);
            textTime = (TextView) itemView.findViewById(R.id.text_my_order_time);
            imageView = (ImageView) itemView.findViewById(R.id.image_taxi_type);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBarFinishedOrderList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_finished_orders_list, parent, false);
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
        viewHolder.textTime.setText(dataSet.get(position).getStartTime().toString());
        viewHolder.ratingBar.setRating(dataSet.get(position).getRating());

        switch (dataSet.get(position).getTaxiType()) {
            case NANO:
                viewHolder.imageView.setImageResource(R.drawable.ic_nano);
                break;
            case CAR:
                viewHolder.imageView.setImageResource(R.drawable.ic_car);
                break;
            case VAN:
                viewHolder.imageView.setImageResource(R.drawable.ic_van);
                break;
        }

        viewHolder.linearLayoutOrderListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FinishedOrderActivity.class);
                intent.putExtra("order", dataSet.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
