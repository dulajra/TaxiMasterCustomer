package com.innocept.taximastercustomer.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.User;
import com.squareup.picasso.Picasso;

/**
 * Created by dulaj on 8/6/16.
 */
public class MyProfileFragment extends Fragment {

    private ImageView imageView;
    private TextView textViewName;
    private TextView textViewPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = (ImageView)rootView.findViewById(R.id.imageViewProfilePicture);
        textViewName = (TextView)rootView.findViewById(R.id.text_name);
        textViewPhone = (TextView)rootView.findViewById(R.id.text_phone);

        User user = ApplicationPreferences.getUser();
        Picasso.with(getActivity())
                .load(user.getImage())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(imageView);
        textViewName.setText(user.getFullName());
        textViewPhone.setText(user.getPhone());
        return rootView;
    }
}
