package com.innocept.taximastercustomer.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.User;
import com.innocept.taximastercustomer.ui.fragment.FavoritesFragment;
import com.innocept.taximastercustomer.ui.fragment.MyOrdersFragment;
import com.innocept.taximastercustomer.ui.fragment.MyPreferenceFragment;
import com.innocept.taximastercustomer.ui.fragment.MyProfileFragment;
import com.innocept.taximastercustomer.ui.fragment.OfferFragment;
import com.innocept.taximastercustomer.ui.fragment.PricingFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Dulaj on 16-Aug-16.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String DEBUG_TAG = HomeActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView imageViewProfilePicture;
    private TextView textViewName, textViewPhone;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;
    private ProgressDialog progressDialog;

    private boolean doubleBackToExitPressedOnce;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NewOrderActivity.class));
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        imageViewProfilePicture = (ImageView) headerView.findViewById(R.id.imageViewProfilePicture);
        textViewName = (TextView) headerView.findViewById(R.id.textViewName);
        textViewPhone = (TextView) headerView.findViewById(R.id.textViewPhone);

        user = ApplicationPreferences.getUser();
        if (user != null) {
            Picasso.with(HomeActivity.this)
                    .load(user.getImage())
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(imageViewProfilePicture);
            textViewName.setText(user.getFullName());
            textViewPhone.setText(user.getPhone());
        }

        if (savedInstanceState == null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MyOrdersFragment()).commit();
            }
        }

        Intent intent = getIntent();
        onNavigationItemSelected(navigationView.getMenu().getItem(intent.getIntExtra("navigationItem", 0)));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case R.id.fragment_my_orders:
                fragment = new MyOrdersFragment();
                break;
            case R.id.fragment_favorites:
                fragment = new FavoritesFragment();
                break;
            case R.id.fragment_pricing:
                fragment = new PricingFragment();
                break;
            case R.id.fragment_offers:
                fragment = new OfferFragment();
                break;
            case R.id.fragment_contact_us:
                fragment = new FavoritesFragment();
                break;
            case R.id.fragment_profile:
                fragment = new MyProfileFragment();
                break;
            case R.id.fragment_settings:
                startActivity(new Intent(HomeActivity.this, PreferenceActivity.class));
                break;
            case R.id.fragment_log_out:
                logout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            if (fragment.getId() == R.id.fragment_profile) {
                toolbar.setTitle(user.getFullName());
            } else {
                toolbar.setTitle(item.getTitle());
            }
        } else {
            Log.e(DEBUG_TAG, "Error in creating fragment: fragment=null");
        }

        return true;
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        displayLoadingDialog(null, "Signing out...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                        ApplicationPreferences.saveUser(null);
                                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                            }
                        }, 1500);
                    }
                })
                .show();
    }

    public void displayLoadingDialog(String title, String message) {
        this.progressDialog = new ProgressDialog(this);
        if (title != null && title.length() > 0) {
            this.progressDialog.setTitle(title);
        }
        if (message != null && message.length() > 0) {
            this.progressDialog.setMessage(message);
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void closeProgressDialog() {
        this.progressDialog.dismiss();
    }
}
