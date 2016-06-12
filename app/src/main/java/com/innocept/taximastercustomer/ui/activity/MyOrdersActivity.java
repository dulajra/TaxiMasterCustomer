package com.innocept.taximastercustomer.ui.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.ui.fragment.FinishedOrderFragment;
import com.innocept.taximastercustomer.ui.fragment.OnGoingOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dulaj on 5/25/16.
 */
public class MyOrdersActivity extends AppCompatActivity {

    private final String DEBUG_TAG = MyOrdersActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Orders");

        viewPager = (ViewPager) findViewById(R.id.viewpager_my_orders);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("finish", false)){
            viewPager.setCurrentItem(1);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OnGoingOrderFragment(), "ON GOING");
        adapter.addFragment(new FinishedOrderFragment(), "FINISHED");
        viewPager.setAdapter(adapter);
    }

    public void openNewOrderActivity(View view) {
        startActivity(new Intent(MyOrdersActivity.this, NewOrderActivity.class));
        this.finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
