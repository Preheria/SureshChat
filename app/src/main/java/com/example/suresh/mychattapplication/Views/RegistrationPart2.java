package com.example.suresh.mychattapplication.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.suresh.mychattapplication.R;

public class RegistrationPart2 extends AppCompatActivity implements View.OnClickListener,ActivityHelper{

    private Intent receivedIntent;
    private Bundle receivedDataBundle;

    private AutoCompleteTextView txtEmail;
    private AutoCompleteTextView txtUsername;
    private AutoCompleteTextView txtPassword1;
    private AutoCompleteTextView txtPassword2;
    private CheckBox chkBox1;
    private Button btnSignup;

    @Override
    public void initializeControls(){

        txtEmail=findViewById(R.id.email);
        txtUsername=findViewById(R.id.username);
        txtPassword1=findViewById(R.id.password1);
        txtPassword2=findViewById(R.id.password2);
        chkBox1=findViewById(R.id.checkBoxConfirm);
        btnSignup=findViewById(R.id.signUpButton);
        btnSignup.setOnClickListener(this);
    }


    @Override
    public boolean validateFields(){

        if(TextUtils.isEmpty(txtEmail.getText())) {
            txtEmail.setError("Email is required");
            return false;
        }
        else if(!txtEmail.getText().toString().contains("@")){
            txtEmail.setError("Invalid Email");
            return false;
        }
        else if(TextUtils.isEmpty(txtUsername.getText())) {
            txtUsername.setError("Username required");
            return false;
        }
        else if(TextUtils.isEmpty(txtPassword1.getText())){
            txtPassword1.setError("Password is required");
            return false;
        }
        else if(TextUtils.isEmpty(txtPassword2.getText())){
            txtPassword2.setError("confirm password!");
            return false;
        }
        else if(!txtPassword1.getText().toString().equals(txtPassword2.getText().toString())) {
            txtPassword2.setError("Password not matched!");
            return false;
        }
        else if(!chkBox1.isChecked()){

            Toast.makeText(this,"Please, select check box to confirm",Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        initializeControls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part2);
        setTitle("User Registration");
        receivedDataBundle=getIntent().getExtras();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.signUpButton:
                if(validateFields())
                    Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this,"not ok",Toast.LENGTH_LONG).show();

                break;
            default:
                break;
        }
    }
}
