package com.example.suresh.mychattapplication.Controllers;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.suresh.mychattapplication.Controllers.Fragments.FragmentManagerAdapter;
import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.FirebaseException;

public class HomePage extends AppCompatActivity implements CommonActivity {

    private FirebaseDAO firebaseDAO;
    private ViewPager viewPager;
    private FragmentManagerAdapter fp_adapter;
    private TabLayout tabLayout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initializeControls();

    }

    @Override
    public void initializeControls() {

        //redundant but necessary statement
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();

        tabLayout=findViewById(R.id.mainTabLayout);
        viewPager=findViewById(R.id.homepageViewPager);
        fp_adapter=new FragmentManagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fp_adapter);
        tabLayout.setupWithViewPager(viewPager);

        user=new User(firebaseDAO.getFirebaseUser().getUid());

    }

    @Override
    public boolean validateFields() {
        return false;
    }


    //method for creating menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_app_menu,menu);

        return  true;
    }


    //event listener for menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            boolean check;
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){

            case R.id.logout:

                    try {
                        firebaseDAO.userLogOut();
                        check=true;
                    }
                    catch (FirebaseException fe){
                        Toast.makeText(HomePage.this,
                                "Can't log right now please try again later",
                                Toast.LENGTH_LONG).show();
                        check=false;
                    }
                //to ensure that user is properly logged out

                if(check) {
                    FirebaseDAO.firebaseDAOObject = null;
                    //taking back to main activity
                    Intent i = new Intent(HomePage.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                break;

            case R.id.setting:
                Toast.makeText(this,"sett clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.myprofile:
                startActivity(new Intent(HomePage.this,Profile.class));
                break;
            default:
                    break;
        }


        return true;
    }
}
