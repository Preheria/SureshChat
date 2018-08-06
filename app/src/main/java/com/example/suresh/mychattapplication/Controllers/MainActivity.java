package com.example.suresh.mychattapplication.Controllers;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.example.suresh.mychattapplication.Models.Services.DeviceTokenService;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CommonActivity{

    private ActionBar actionBar; //this is required to remove action bar
    private Button btnLogin;
    private TextView txtSignUp;
    private TextInputEditText email;
    private TextInputEditText pword;
    private boolean flag;
    private FirebaseDAO firebaseDAO;
    private View pbar;
    private View mainView;
    private User user;

    @Override
    public void initializeViews() {

        try {
            email = findViewById(R.id.loginEmail);
            pword = findViewById(R.id.loginPassword);

            btnLogin = findViewById(R.id.buttonLogin);
            btnLogin.setOnClickListener(this);

            txtSignUp = findViewById(R.id.txtSignUp);
            txtSignUp.setOnClickListener(this);

            pbar = findViewById(R.id.progressBarMainActivity);
            mainView = findViewById(R.id.mainView);
        }
        catch (Exception e){
                Log.e("ERROR",e.getMessage());
        }
    }

    //function for validating username and password fields
    @Override
    public boolean validateFields() {
        try{
        if(TextUtils.isEmpty(email.getText())){
            email.setError("email is required");
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
        }}
        catch (Exception e){
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        }
        catch (Exception e){
            Log.e("startup problem",e.getMessage());
        }

        }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonLogin:

                if(validateFields()) {
                    mainView.setVisibility(View.GONE);
                    pbar.setVisibility(View.VISIBLE);

                    new AsyncTask<String, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(String... strings) {

                            firebaseDAO = FirebaseDAO.getFirebaseDAOObject();
                            firebaseDAO.getAuthenticationObject()
                                    .signInWithEmailAndPassword(strings[0],strings[1])
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String token=(new DeviceTokenService()).getRefreshedToken();
                                                firebaseDAO.saveDeviceTokens(
                                                        getApplicationContext(),
                                                        firebaseDAO.getAuthenticationObject().getCurrentUser().getUid(),
                                                        token
                                                );

                                        FirebaseDAO.UID=firebaseDAO.getAuthenticationObject().getCurrentUser().getUid();
                                        firebaseDAO.getDbReference()
                                                        .child("users")
                                                        .child(FirebaseDAO.UID)
                                                        .child("online")
                                                        .addValueEventListener(FirebaseDAO.valueEventListener);

                            Intent i = new Intent(MainActivity.this, HomeActivity.class);
                            i.putExtra("UID",firebaseDAO.getAuthenticationObject().getCurrentUser().getUid());
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                                            }
                                            else {
                                                mainView.setVisibility(View.VISIBLE);
                                                pbar.setVisibility(View.GONE);
                                                Toast.makeText(MainActivity.this,
                                                        "Some error occured, Please, try again later!",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                        }
                                    });
                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                        }

                    }.execute(email.getText().toString().trim(), pword.getText().toString());
                }

                break;

            case R.id.txtSignUp:
                Intent i=new Intent(MainActivity.this,RegistrationPart1Activity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }}





















