package com.innocept.taximastercustomer.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.data.DatabaseHandler;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.ui.adapters.MyOrderAdapter;

import java.util.List;

/**
 * Created by dulaj on 5/24/16.
 */
public class FinishedOrderFragment extends Fragment {

    private final String DEBUG_TAG = FinishedOrderFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Order> dataSet;
    DatabaseHandler db;

    public FinishedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(ApplicationContext.getContext());
        Intent intent = (getActivity()).getIntent();

        if(intent.getBooleanExtra("finish", false)){
            db.updateOrderState(intent.getIntExtra("id", -1), Order.OrderState.FINISHED);
        }

        dataSet = db.getAllOrders(new Order.OrderState[]{Order.OrderState.FINISHED});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_order_tab, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_my_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyOrderAdapter(getActivity(), dataSet);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final Order order = dataSet.get(viewHolder.getAdapterPosition());
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Do you want to delete the order")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteOrder(order.getId());
                                dataSet.remove(viewHolder.getAdapterPosition());
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create();
                dialog.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

}
