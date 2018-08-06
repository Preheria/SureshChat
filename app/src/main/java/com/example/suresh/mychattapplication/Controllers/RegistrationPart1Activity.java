package com.example.suresh.mychattapplication.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.example.suresh.mychattapplication.R;
import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationPart1Activity extends AppCompatActivity implements View.OnClickListener,CommonActivity {

    private View progressBar;
    private Button nextButton;
    private  TextInputEditText txtFname;
    private  TextInputEditText txtLname;
    private  TextInputEditText txtstate;
    private  TextInputEditText txtHomeAddress;
    private  TextInputEditText txtPhone;
    private View regFrom;
    private String countyName;
    private String homeAddress;
    private String phone;
    private HashMap<String ,String> dataPart1;
    private  Spinner countryList;
    private  Spinner dayList;
    private  Spinner monthList;
    private  Spinner yearList;

    private ArrayList<Integer> years;
    private ArrayList<Integer> days;

    private ArrayAdapter<Integer> adapter1;
    private ArrayAdapter<Integer> adapter2;

    protected Intent i;

    //wiring of controls from layout files to corresponding java control objects

    @Override
    public void initializeViews(){
        //casting is necessary here
        txtFname=findViewById(R.id.firstName);
        txtLname=findViewById(R.id.lastName);
        dataPart1=new HashMap<>();
        countryList=findViewById(R.id.countryList);
        yearList=findViewById(R.id.spin_year);
        monthList=findViewById(R.id.spin_month);
        dayList=findViewById(R.id.spin_day);
        txtstate=findViewById(R.id.address);
        txtHomeAddress=findViewById(R.id.homeAddress);
        txtPhone=findViewById(R.id.phone);

        nextButton=findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);

        regFrom=findViewById(R.id.registrationForm1);
        progressBar=findViewById(R.id.progressBar);
        years=new ArrayList<>();
        //birth year generation
        for(int i=1975;i<=2010;i++)
        {
            years.add(i);
        }

        days=new ArrayList<>();
        //days generation using loop
        for(int i=1;i<=31;i++)
        {
            days.add(i);
        }

        adapter1=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,years);
        adapter2=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,days);

        //adding year values to yearList spinner
        yearList.setAdapter(adapter1);

        //adding year values to dayList spinner
        dayList.setAdapter(adapter2);

    }

    @Override
    public boolean validateFields(){
        if(TextUtils.isEmpty(txtFname.getText())) {
            txtFname.setError("First name is missing");
            return false;
        }
        else if(TextUtils.isEmpty(txtLname.getText()))
        {
            txtLname.setError("Last name is missing");
            return false;
        }
        else if(TextUtils.isEmpty(txtstate.getText()))
        {
            txtstate.setError("State name is missing");
            return false;
        }
        else if(TextUtils.isEmpty(txtHomeAddress.getText()))
        {
            txtHomeAddress.setError("Street address is missing");
            return false;
        }
        else if(TextUtils.isEmpty(txtPhone.getText()))
        {
            txtPhone.setError("Phone number is missing");
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part1);
        setTitle("User Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeViews();
        }

    @Override
    protected void onResume(){
        super.onResume();
        regFrom.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

                if(validateFields()){
                    regFrom.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    @SuppressLint("StaticFieldLeak")
             AsyncTask<HashMap<String ,String >,Void,String> task1=new AsyncTask<HashMap<String ,String >,Void,String> (){

                         @Override
                        protected String doInBackground(HashMap<String, String>... hashMaps) {
                            i=new Intent(RegistrationPart1Activity.this,RegistrationPart2Activity.class);
                            hashMaps[0].put("fname",txtFname.getText().toString().trim());
                            hashMaps[0].put("lname",txtLname.getText().toString().trim());
                            hashMaps[0].put("DOB",yearList.getSelectedItem().toString()+"-"
                                    +monthList.getSelectedItem().toString()+"-"
                                    +dayList.getSelectedItem().toString());
                            hashMaps[0].put("country",countryList.getSelectedItem().toString());
                            hashMaps[0].put("state",txtstate.getText().toString().trim());
                            hashMaps[0].put("homeAddress",txtHomeAddress.getText().toString().trim());
                            hashMaps[0].put("phone",txtPhone.getText().toString().trim());

                            i.putExtra("dataMap",hashMaps[0]);

                            return "ok";
                        }

                        @Override
                        protected void onPreExecute(){
                        }

                        @Override
                        protected void onPostExecute(String s){
                            startActivity(i);
                        }
                    };

                    task1.execute(dataPart1);
                }
            }
}


















