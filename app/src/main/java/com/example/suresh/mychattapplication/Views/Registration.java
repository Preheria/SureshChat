package com.example.suresh.mychattapplication.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.suresh.mychattapplication.R;

import java.util.ArrayList;

public class Registration extends AppCompatActivity implements View.OnClickListener,ActivityHelper {

    private Button nextButton;
    private AutoCompleteTextView txtFname;
    private AutoCompleteTextView txtLname;
    private AutoCompleteTextView txtstate;
    private AutoCompleteTextView txtHomeAddress;
    private AutoCompleteTextView txtPhone;

    private String countyName;
    private String homeAddress;
    private String phone;

    private Spinner countryList;
    private Spinner dayList;
    private Spinner monthList;
    private Spinner yearList;

    private ArrayList<Integer> years;
    private ArrayList<Integer> days;

    private ArrayAdapter<Integer> adapter1;
    private ArrayAdapter<Integer> adapter2;

    private Intent i;

    //wiring of controls from layout files to corresponding java control objects

    @Override
    public void initializeControls(){
        txtFname=findViewById(R.id.firstName);
        txtLname=findViewById(R.id.lastName);

        countryList=findViewById(R.id.countryList);
        yearList=findViewById(R.id.spin_year);
        monthList=findViewById(R.id.spin_month);
        dayList=findViewById(R.id.spin_day);

        txtstate=findViewById(R.id.address);
        txtHomeAddress=findViewById(R.id.homeAddress);
        txtPhone=findViewById(R.id.phone);

        nextButton=(Button)findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);

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

        adapter1=new ArrayAdapter<Integer>(this,R.layout.support_simple_spinner_dropdown_item,years);
        adapter2=new ArrayAdapter<Integer>(this,R.layout.support_simple_spinner_dropdown_item,days);

        yearList.setAdapter(adapter1);
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
        else

        return true;
    }


    @Override
    protected void onStart(){
        super.onStart();
        initializeControls();
        Log.e("TAG","m done");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part1);
        setTitle("User Registration");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonNext:

                if(validateFields()){
                    i=new Intent(Registration.this,RegistrationPart2.class);
                    i.putExtra("firstName",txtFname.getText().toString().trim());
                    i.putExtra("lastName",txtLname.getText().toString().trim());
                    i.putExtra("DOB-yy",yearList.getSelectedItem().toString());
                    i.putExtra("DOB-mm",monthList.getSelectedItem().toString());
                    i.putExtra("DOB-dd",dayList.getSelectedItem().toString());
                    i.putExtra("conutry",countryList.getSelectedItem().toString());
                    i.putExtra("state",txtstate.getText().toString().trim());
                    i.putExtra("homeAddress",txtHomeAddress.toString().trim());
                    i.putExtra("phone",txtPhone.getText().toString().trim());
                    startActivity(i);
                }


                break;
            default:
                break;
        }
    }
}
