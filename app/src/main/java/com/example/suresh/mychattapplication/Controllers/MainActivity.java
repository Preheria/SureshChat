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


import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;

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
        getSupportActionBar().setTitle("Home - MyChat App");
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
                            user.setEmail(strings[0].trim());
                            user.setPassword(strings[1].trim());

                            firebaseDAO=FirebaseDAO.getFirebaseDAOObject();

                            firebaseDAO.userLogin(user);

                            return firebaseDAO.getFirebaseUser() != null;


                        }

                        @Override
                        protected void onPostExecute(Boolean result){

                            if(result) {
                               // pbar.setVisibility(View.GONE);
                                //mainView.setVisibility(View.VISIBLE);
                                Intent i = new Intent(MainActivity.this, HomePage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                user=null;
                                startActivity(i);
                                finish();
                            }

                        }
                    };
                    task1.execute(email.getText().toString(),pword.getText().toString());


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
