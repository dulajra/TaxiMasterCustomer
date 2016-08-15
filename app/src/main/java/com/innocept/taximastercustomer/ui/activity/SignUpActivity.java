package com.innocept.taximastercustomer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.presenter.SignUpPresenter;

/**
 * Created by Dulaj on 14-Aug-16.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    SignUpPresenter signUpPresenter;

    private Toolbar toolbar;
    private EditText inputPhone, inputPassword, inputConfirmPassword, inputFirstName, inputLastName;
    private TextInputLayout inputLayoutPhone, inputLayoutPassword, inputLayoutConfirmPassword, inputLayoutFirstName, inputLayoutLastName;
    private Button btnSignUp;
    CoordinatorLayout coordinatorLayoutParent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (signUpPresenter == null) {
            signUpPresenter = SignUpPresenter.getInstance().getInstance();
        }
        signUpPresenter.setView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);

        inputLayoutFirstName = (TextInputLayout) findViewById(R.id.input_layout_first_name);
        inputLayoutLastName = (TextInputLayout) findViewById(R.id.input_layout_last_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.input_layout_confirm_password);
        inputFirstName = (EditText) findViewById(R.id.input_first_name);
        inputLastName = (EditText) findViewById(R.id.input_last_name);
        inputPhone = (EditText) findViewById(R.id.input_phone);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        coordinatorLayoutParent = (CoordinatorLayout) findViewById(R.id.coordinator_parent);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_sign_in);

        inputFirstName.addTextChangedListener(new MyTextWatcher(inputFirstName));
        inputLastName.addTextChangedListener(new MyTextWatcher(inputLastName));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputConfirmPassword.addTextChangedListener(new MyTextWatcher(inputConfirmPassword));

        btnSignUp.setOnClickListener(this);
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if(!validateFirstName()) return;
        else

        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(inputConfirmPassword.getWindowToken(), 0);

        signUp();
    }

    private boolean validateFirstName(){
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            inputLayoutFirstName.setError(getString(R.string.err_msg_first_name));
            requestFocus(inputFirstName);
            return false;
        } else {
            inputLayoutFirstName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLastName(){
        if (inputLastName.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(getString(R.string.err_msg_last_name));
            requestFocus(inputLastName);
            return false;
        } else {
            inputLayoutLastName.setErrorEnabled(false);
            return  true;
        }
    }

    private boolean validatePhone(){
        if (inputFirstName.getText().toString().trim().length() < 9) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
            return  true;
        }
    }

    private boolean validatePassword(){
        if (inputPassword.getText().toString().trim().length() < 6) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password_sign_up));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateConfirmPassword(){
        if (inputConfirmPassword.getText().toString().equals(inputPassword.getText().toString())) {
            inputLayoutConfirmPassword.setError(getString(R.string.err_msg_conform_password));
            requestFocus(inputConfirmPassword);
            return false;
        } else {
            inputLayoutConfirmPassword.setErrorEnabled(false);
            return true;
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                submitForm();
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_phone:
                    validatePhone();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    public void signUp() {
        progressBar.setVisibility(View.VISIBLE);
        loginPresenter.signIn(inputPhone.getText().toString().trim(), inputPassword.getText().toString().trim());
    }

    public void onSignInSuccess() {
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(LoginActivity.this, NewOrderActivity.class));
        this.finish();
    }

    public void onSignInFailed(String error) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(coordinatorLayoutParent, error, Snackbar.LENGTH_LONG).show();
    }
}
