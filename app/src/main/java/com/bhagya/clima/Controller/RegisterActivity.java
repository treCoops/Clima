package com.bhagya.clima.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhagya.clima.Helper.ToolTip;
import com.bhagya.clima.Helper.Validator;
import com.bhagya.clima.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class RegisterActivity extends AppCompatActivity {

    EditText txtFullName, txtRegisterEmailAddress, txtPassword, txtConfirmPassword;
    ImageView imgFullNameStatus, imgEmailStatus, imgPasswordStatus, imgConfirmPasswordStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        TextView txtSignIn = findViewById(R.id.txtSignIn);
        Button btnRegister = findViewById(R.id.btnRegister);

        txtFullName = findViewById(R.id.txtFullName);
        txtRegisterEmailAddress = findViewById(R.id.txtRegisterEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        imgFullNameStatus = findViewById(R.id.imgFullNameStatus);
        imgEmailStatus = findViewById(R.id.imgEmailStatus);
        imgPasswordStatus = findViewById(R.id.imgPasswordStatus);
        imgConfirmPasswordStatus = findViewById(R.id.imgConfirmPasswordStatus);

        txtSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            Animatoo.animateSlideRight(RegisterActivity.this);
            finishAffinity();
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Validator.checkEmpty(txtFullName.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtFullName, "Full name required!");
                    vibrator.vibrate(5);
                    return;
                }
                if(!Validator.validatePersonName(txtFullName.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtFullName, "Full name required without special characters!");
                    vibrator.vibrate(5);
                    return;
                }

                if(Validator.checkEmpty(txtRegisterEmailAddress.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtRegisterEmailAddress, "Email address required!");
                    vibrator.vibrate(5);
                    return;
                }

                if(!Validator.validateEmail(txtRegisterEmailAddress.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtRegisterEmailAddress, "Email address must be well formatted!");
                    vibrator.vibrate(5);
                    return;
                }

                if(Validator.checkEmpty(txtPassword.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtPassword, "Password required!");
                    vibrator.vibrate(5);
                    return;
                }

                if(!Validator.textLength(txtPassword.getText().toString().trim(), 6)){
                    ToolTip.show(getApplicationContext(), txtPassword, "Password must have more than 6 characters!");
                    vibrator.vibrate(5);
                    return;
                }

                if(Validator.checkEmpty(txtConfirmPassword.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtConfirmPassword, "Confirm password required!");
                    vibrator.vibrate(5);
                    return;
                }

                if(!Validator.textLength(txtConfirmPassword.getText().toString().trim(), 6)){
                    ToolTip.show(getApplicationContext(), txtConfirmPassword, "Confirm password must have more than 6 characters!");
                    vibrator.vibrate(5);
                    return;
                }

                if(!Validator.checkTwoSame(txtPassword.getText().toString().trim(), txtConfirmPassword.getText().toString().trim())){
                    ToolTip.show(getApplicationContext(), txtPassword, "Password and confirm password are not same!");
                    ToolTip.show(getApplicationContext(), txtConfirmPassword, "Password and confirm password are not same!");
                    vibrator.vibrate(5);
                    return;
                }
            }
        });

        activeTextChangeListeners();
    }

    void activeTextChangeListeners(){

        txtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(Validator.checkEmpty(txtFullName.getText().toString().trim())){
                    imgFullNameStatus.setImageResource(R.drawable.caution);
                } else if(!Validator.validatePersonName(txtFullName.getText().toString().trim())){
                    imgFullNameStatus.setImageResource(R.drawable.cancel);
                } else {
                    imgFullNameStatus.setImageResource(R.drawable.check);
                }

            }
        });

        txtRegisterEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(Validator.checkEmpty(txtRegisterEmailAddress.getText().toString().trim())){
                    imgEmailStatus.setImageResource(R.drawable.caution);
                } else if(!Validator.validateEmail(txtRegisterEmailAddress.getText().toString().trim())){
                    imgEmailStatus.setImageResource(R.drawable.cancel);
                } else {
                    imgEmailStatus.setImageResource(R.drawable.check);
                }

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Validator.checkEmpty(txtPassword.getText().toString().trim())){
                    imgPasswordStatus.setImageResource(R.drawable.caution);
                } else if(!Validator.textLength(txtPassword.getText().toString().trim(), 6)){
                    imgPasswordStatus.setImageResource(R.drawable.cancel);
                }
                else {
                    imgPasswordStatus.setImageResource(R.drawable.check);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Validator.checkEmpty(txtConfirmPassword.getText().toString().trim())){
                    imgConfirmPasswordStatus.setImageResource(R.drawable.caution);
                } else if(!Validator.textLength(txtConfirmPassword.getText().toString().trim(), 6)){
                    imgConfirmPasswordStatus.setImageResource(R.drawable.cancel);
                }
                else {
                    imgConfirmPasswordStatus.setImageResource(R.drawable.check);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        Animatoo.animateSlideRight(RegisterActivity.this);
        finishAffinity();
    }
}