package com.example.suresh.mychattapplication.Controllers;

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
        btnSignup=findViewById(R.id.signUpButton);
        btnSignup.setOnClickListener(this);

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
        initializeControls();
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.signUpButton:
                if(validateFields()){

                    user=new User();

                    userSignupData.put("email",txtEmail.getText().toString().trim());
                    userSignupData.put("username",txtUsername.getText().toString().trim());
                    userSignupData.put("password",txtPassword2.getText().toString().trim());
                    user.setUserdata(userSignupData);

                    firebaseDAO=FirebaseDAO.getFirebaseDAOObject();

                    //user signup
                    firebaseDAO.userSignup(user);


                }
                else
                    Toast.makeText(this,"not ok",Toast.LENGTH_LONG).show();

                break;
            default:
                break;
        }
    }
}
