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
import android.widget.TextView;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Offer;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.ui.activity.CurrentOrderActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    Context context;
    private List<Offer> dataSet;

    public OfferAdapter(Context context, List<Offer> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutOrderListItem;
        TextView textTitle;
        TextView textDescription;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayoutOrderListItem = (LinearLayout) itemView.findViewById(R.id.linear_taxi_list_item);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textDescription = (TextView) itemView.findViewById(R.id.text_description);
            imageView = (ImageView)itemView.findViewById(R.id.image_offer);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_offers_list, parent, false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 3, 0, 3);
        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textTitle.setText(dataSet.get(position).getTitle());
        viewHolder.textDescription.setText(dataSet.get(position).getDescription());
        Picasso.with(context)
                .load(dataSet.get(position).getImageUrl())
                .placeholder(R.drawable.ic_offer)
                .error(R.drawable.ic_offer)
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
