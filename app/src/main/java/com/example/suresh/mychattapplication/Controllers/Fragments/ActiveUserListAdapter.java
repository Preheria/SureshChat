package com.example.suresh.mychattapplication.Controllers.Fragments;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveUserListAdapter extends ArrayAdapter<User> {

    private Context context;
    private int resource;
    private ArrayList<User> userList;

    public ActiveUserListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> userlist) {
        super(context, resource, userlist);
        this.context=context;
        this.resource=resource;
        this.userList=userlist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView activeUserFullName;
        TextView activeUserId;
        TextView imageURI;

        CircleImageView activeUserThumbnail;
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null);
        activeUserFullName=view.findViewById(R.id.fullNameActiveUser);
        activeUserThumbnail=view.findViewById(R.id.thumbnailActiveUser);
        activeUserId=view.findViewById(R.id.activeUserID);
        imageURI=view.findViewById(R.id.activeUserThumbnailURI);

        User user=userList.get(position);
        activeUserFullName.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
        activeUserId.setText(user.getUserID());
        imageURI.setText(user.getPp_path());
        if(user.getPp_path().equals("")){
            activeUserThumbnail.setImageResource(R.drawable.ic_profile_male);
        }
        else{
            Picasso.get().load(user.getPp_path()).into(activeUserThumbnail);
        }

        return view;
    }
}

