package com.example.suresh.mychattapplication.Controllers;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewListAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private ArrayList<User> userList;

    public SearchViewListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<User> userList) {
        super(context,resource,userList);
        this.context=context;
        this.resource=resource;
        this.userList=userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);

        //each item will be written as a view which needs to be returned
        View view=inflater.inflate(resource,null);

        //initialization of View elements
        TextView fullName=view.findViewById(R.id.fullName);
        TextView location=view.findViewById(R.id.location);
        TextView imageURI=view.findViewById(R.id.IMAGE_URI);
        TextView uid=view.findViewById(R.id.uid);
        CircleImageView thumbnail=view.findViewById(R.id.thumbnail);
        ViewGroup parentView=view.findViewById(R.id.anItem);

        //getting a user at a particular index
        User user=userList.get(position);

        //binding model data with view data
        fullName.setText(user.getFirstName()+" "+user.getLastName());
        location.setText(user.getState()+" , "+user.getCountry());
        uid.setText(user.getUserID());
        imageURI.setText(user.getPp_path());

        if(user.getPp_path().equals("")){
            thumbnail.setImageResource(R.drawable.ic_profile_male);
        }
        else{
            Picasso.get().load(user.getPp_path()).into(thumbnail);
        }

        return view;
    }
}















