package com.example.suresh.mychattapplication.Controllers;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suresh.mychattapplication.Controllers.Fragments.FragmentManagerAdapter;
import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class HomeActivity extends AppCompatActivity implements CommonActivity {

    private FirebaseDAO firebaseDAO;
    private ViewPager viewPager;
    private FragmentManagerAdapter fp_adapter;
    private TabLayout tabLayout;
    private User user;
    private ArrayList<User> userList;
    private SearchViewListAdapter adapter;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            handleIntent(getIntent());
        }
        catch (Exception e){
            System.out.println("Exception caught : "+e.getMessage());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {setIntent(intent);
        handleIntent(intent);}
        catch (Exception e){
            System.out.println("Null pointer exception handled");
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }

    }

    @Override
    public void initializeViews() {

        //redundant but necessary statement
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
        tabLayout=findViewById(R.id.mainTabLayout);
        viewPager=findViewById(R.id.homepageViewPager);
        fp_adapter=new FragmentManagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fp_adapter);
        tabLayout.setupWithViewPager(viewPager);

        //creating user with reference to FirebaseAuth.current user's id
        user=new User(FirebaseDAO.UID);
        userList=new ArrayList<User>();

        //method for creating and initializing list view and its operaitons
        configureListView();
        adapter=new SearchViewListAdapter(HomeActivity.this,R.layout.list_item,userList);
        listView.setAdapter(adapter);

    }

    private void configureSearchViewInterface(Menu menu,MenuItem searchMenu) {

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenu.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                userList.clear();
                adapter.notifyDataSetChanged();
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);

                return true;
            }
        });

        searchView = (SearchView) menu.findItem(R.id.searchTool).getActionView();
        searchView.setIconified(true);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                userList.clear();
                adapter.notifyDataSetChanged();

                firebaseDAO.getDbReference()
                        .child("users")
                        .orderByChild("fName")
                        .startAt(newText)
                        .endAt(newText+"\uf8ff")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot aChild:dataSnapshot.getChildren()
                                        ) {
                                    if (!aChild.child("email")
                                            .getValue(String.class)
                                            .equalsIgnoreCase(firebaseDAO.getAuthenticationObject()
                                                                .getCurrentUser().getEmail())) {

                                        User user = new User(
                                                aChild.getKey(),
                                                aChild.child("fName").getValue(String.class),
                                                aChild.child("lName").getValue(String.class),
                                                aChild.child("country").getValue(String.class),
                                                aChild.child("state").getValue(String.class),
                                                aChild.child("pp_path").getValue(String.class)

                                        );
                                        userList.add(user);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                return true;
            }
        });
    }

    private void configureListView() {
        listView=findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           //this method opens userProfile activity and supplies the userid as refernce to that activity
           //full name is required to rename the Appbar title of the user profile activity
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent userProfileIntent=new Intent(HomeActivity.this,UserProfileActivity.class);
                userProfileIntent.putExtra("targetUserID",((TextView)view.findViewById(R.id.uid)).getText());
                userProfileIntent.putExtra("fullname",((TextView)view.findViewById(R.id.fullName)).getText());
                userProfileIntent.putExtra("address",((TextView)view.findViewById(R.id.location)).getText());
                userProfileIntent.putExtra("imageURI",((TextView)view.findViewById(R.id.IMAGE_URI)).getText());
                userProfileIntent.putExtra("loggedInUserID",user.getUserID());
                startActivity(userProfileIntent);
            }
        });
    }

    @Override
    public boolean validateFields() {
        return false;
    }

    //method for creating menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.my_app_menu, menu);
            MenuItem item = menu.findItem(R.id.searchTool);

            configureSearchViewInterface(menu, item);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("MyChatt Appp home");
        }
        initializeViews();
    }

    //method to let know that textbox has gained focus
    @Override
    public boolean onSearchRequested() {
        //invocation of this method means that Searchbox has gained focused
        //this method ends by calling super class method implementation
        return super.onSearchRequested();
    }

    //event listener for menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item==null){
            return false;
        }
        super.onOptionsItemSelected(item);

        boolean check;
        switch (item.getItemId()){

            case R.id.searchTool:
                break;

            case R.id.logout:

                //removing event listener so that online status could be changed to false later
                firebaseDAO.getDbReference().child("users")
                        .child(FirebaseDAO.UID)
                        .child("online")
                        .removeEventListener(FirebaseDAO.valueEventListener);

                //writing online status as false
                firebaseDAO.getDbReference().child("users")
                        .child(FirebaseDAO.UID)
                        .child("online").setValue(false);

                firebaseDAO.getAuthenticationObject().signOut();

                    firebaseDAO=null;
                    user=null;
                    //taking back to main activity
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                break;

            case R.id.setting:
                Toast.makeText(this,"sett clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.myprofile:
                startActivity(new Intent(HomeActivity.this,Profile.class));
                break;
            default:
                    break;
        }

        return true;
    }
}

