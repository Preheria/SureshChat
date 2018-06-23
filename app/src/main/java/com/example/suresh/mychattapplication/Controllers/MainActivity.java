package com.example.suresh.mychattapplication.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CommonActivity{

    private ActionBar actionBar; //this is required to remove action bar
    private Button btnLogin;
    private TextView txtSignUp;
    private TextInputEditText uname;
    private TextInputEditText pword;

    private View pbar;
    private View mainView;

    private User user;

    @Override
    public void initializeControls() {

        uname=findViewById(R.id.uname);
        pword=findViewById(R.id.pword);

        btnLogin=findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(this);

        txtSignUp=findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        pbar=findViewById(R.id.progressBar);
        mainView=findViewById(R.id.mainView);


    }


    //function for validating username and password fields
    @Override
    public boolean validateFields() {

        if(TextUtils.isEmpty(uname.getText())){
            uname.setError("username is required");
            return false;
        }
        else if(TextUtils.isEmpty(pword.getText())) {
            pword.setError("password is required");
            return false;
        }
        else{
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeControls();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonLogin:

                if(validateFields()){

                    btnLogin.setTextColor(Color.WHITE);
                    btnLogin.setBackgroundResource(R.drawable.clicked_button);
                    mainView.setVisibility(View.GONE);
                    pbar.setVisibility(View.VISIBLE);

                    //this creates a thread that handles the task of verifyng username and passwords.
                    // this is required to reduce the work load to main UU thread.
                    @SuppressLint("StaticFieldLeak")
                    AsyncTask<String, Void, Boolean> task1=new AsyncTask<String,Void,Boolean> (){

                        @Override
                        protected Boolean doInBackground(String... strings) {

                            user=new User();
                            user.userLogin();
                            return true;
                        }
                    };
                    task1.execute(uname.getText().toString(),pword.getText().toString());




                }

                break;

            case R.id.txtSignUp:
                Intent i=new Intent(MainActivity.this,RegistrationPart1.class);

                startActivity(i);
                break;
            default:
                break;
        }

    }




}
