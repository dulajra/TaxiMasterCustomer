package com.innocept.taximastercustomer.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.User;
import com.squareup.picasso.Picasso;

/**
 * @author dulaj
 * @version 1.0.0
 *//**
 * @author dulaj
 * @version 1.0.0
 */
public class MyProfileFragment extends Fragment {

    private ImageView imageView;
    private Toolbar toolbarName;
    private TextView textViewPhone;
    private TextView textViewEmail;
    private TextView textViewAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = (ImageView)rootView.findViewById(R.id.imageViewDriverProfilePicture);
        toolbarName = (Toolbar) rootView.findViewById(R.id.toolbar);
        textViewPhone = (TextView)rootView.findViewById(R.id.phone);
        textViewEmail = (TextView)rootView.findViewById(R.id.email);
        textViewAddress = (TextView)rootView.findViewById(R.id.address);

        User user = ApplicationPreferences.getUser();
        Picasso.with(getActivity())
                .load(user.getImage())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(imageView);
        toolbarName.setTitle(user.getFullName());
        textViewPhone.setText(user.getPhone());
        textViewEmail.setText(user.getEmail());
        textViewAddress.setText(user.getAddress());
        return rootView;
    }
}
