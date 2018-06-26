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
import android.widget.Toast;


import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.FirebaseException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CommonActivity{

    private ActionBar actionBar; //this is required to remove action bar
    private Button btnLogin;
    private TextView txtSignUp;
    private TextInputEditText email;
    private TextInputEditText pword;

    private FirebaseDAO firebaseDAO;

    private View pbar;
    private View mainView;

    private User user;

    @Override
    public void initializeControls() {

        email =findViewById(R.id.loginEmail);
        pword=findViewById(R.id.loginPassword);

        btnLogin=findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(this);

        txtSignUp=findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);

        pbar=findViewById(R.id.progressBarMainActivity);
        mainView=findViewById(R.id.mainView);


    }


    //function for validating username and password fields
    @Override
    public boolean validateFields() {

        if(TextUtils.isEmpty(email.getText())){
            email.setError("username is required");
            return false;
        }
        else if(!email.getText().toString().contains("@")){
            email.setError("Invalid email");
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonLogin:

                if(validateFields()) {

                    btnLogin.setTextColor(Color.WHITE);
                    btnLogin.setBackgroundResource(R.drawable.clicked_button);
                    mainView.setVisibility(View.GONE);
                    pbar.setVisibility(View.VISIBLE);


                    new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... strings) {
                            User user = new User();
                            user.setEmail(strings[0]);

                            user.setPassword(strings[1]);

                            firebaseDAO = FirebaseDAO.getFirebaseDAOObject();

                            return firebaseDAO.userLogin(user);

                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);

                            if (aBoolean) {
                                Intent i = new Intent(MainActivity.this, HomePage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                user = null;
                                startActivity(i);
                                finish();
                            }
                            else {
                                mainView.setVisibility(View.VISIBLE);
                                pbar.setVisibility(View.GONE);
                                firebaseDAO=null;
                                user=null;
                                Toast.makeText(MainActivity.this,
                                        "Some error occured, Please, try again later!",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                    }.execute(email.getText().toString().trim(), pword.getText().toString());
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
