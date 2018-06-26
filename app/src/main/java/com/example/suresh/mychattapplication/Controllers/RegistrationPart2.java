package com.example.suresh.mychattapplication.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;

import java.util.HashMap;

public class RegistrationPart2 extends AppCompatActivity implements View.OnClickListener,CommonActivity {


    private User user;
    private FirebaseDAO firebaseDAO;
    private ProgressBar progressBar;
    private TextInputEditText txtEmail;
    private TextInputEditText txtUsername;
    private TextInputEditText txtPassword1;
    private TextInputEditText txtPassword2;
    private CheckBox chkBox1;
    private View consolidateView;

    //hashmap for storing user registration data from previous activity
    private HashMap<String ,String> userSignupData;
    private Button btnSignup;


    @Override
    public void initializeControls(){

        txtEmail=findViewById(R.id.email);
        txtUsername=findViewById(R.id.username);
        txtPassword1=findViewById(R.id.password1);
        txtPassword2=findViewById(R.id.password2);
        chkBox1=findViewById(R.id.checkBoxConfirm);
        progressBar=findViewById(R.id.progressBarRegActivity);
        btnSignup=findViewById(R.id.signUpButton);
        btnSignup.setOnClickListener(this);
        consolidateView=findViewById(R.id.consolidateView);

        //retrieving userregistration data from previous activity
        userSignupData =(HashMap<String, String>) getIntent().getSerializableExtra("dataMap");

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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part2);
        setTitle("User Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeControls();
        }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.signUpButton:
                if(validateFields()) {

                    progressBar.setVisibility(View.VISIBLE);
                    consolidateView.setVisibility(View.GONE);

                    new AsyncTask<String, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(String... strings) {

                            user = new User();

                            userSignupData.put("email", txtEmail.getText().toString().trim());
                            userSignupData.put("username", txtUsername.getText().toString().trim());
                            userSignupData.put("password", txtPassword2.getText().toString().trim());
                            user.setUserdata(userSignupData);

                            firebaseDAO = FirebaseDAO.getFirebaseDAOObject();

                            //user signup
                            return firebaseDAO.userSignup(user);
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);

                            if (aBoolean) {
                                Intent i = new Intent(RegistrationPart2.this, HomePage.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                user = null;
                                startActivity(i);
                                finish();
                            } else {

                                progressBar.setVisibility(View.GONE);
                                consolidateView.setVisibility(View.VISIBLE);
                                firebaseDAO = null;
                                user = null;
                                Toast.makeText(RegistrationPart2.this,
                                        "Some error occured, pleas re-enter the details and try again",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }


                    }.execute("start");
                }

                break;
            default:
                break;
        }
    }
}
