package com.example.suresh.mychattapplication.Controllers;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements CommonActivity, View.OnClickListener{

    private User user;
    private FirebaseDAO firebaseDAO;
    private TextView tvFullname,tvUsername, tvStatus,tvEmail,tvPhone,tvAddress,tvFriendCount;
    private DatabaseReference dbrf;
    private CircleImageView profilePic;
    private Button btnImgUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("My profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeControls();
    }

    @Override
    public void initializeControls() {
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
        user=new User(FirebaseDAO.UID);

        profilePic=findViewById(R.id.profile_image);
        //holds reference to the database path
        //i.e. /users/(Userid)

        tvFullname=findViewById(R.id.p_fullName);
        tvUsername=findViewById(R.id.p_username);
        tvStatus =findViewById(R.id.p_motto);
        tvEmail=findViewById(R.id.p_email);
        tvPhone=findViewById(R.id.p_phone);
        tvAddress=findViewById(R.id.p_address);
        tvFriendCount=findViewById(R.id.p_friendCount);
        btnImgUpload=findViewById(R.id.btnImgUpload);
        btnImgUpload.setOnClickListener(this);

        dbrf=firebaseDAO.getDbReference().child("users").child(user.getUserID());

        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                    tvFullname.setText(dataSnapshot.child("fName").getValue(String.class)+" "
                            +dataSnapshot.child("lName").getValue(String.class)
                    );

                    tvUsername.setText(dataSnapshot.child("username").getValue(String.class));

                    tvStatus.setText(dataSnapshot.child("status").getValue(String.class));

                    tvEmail.setText(dataSnapshot.child("email").getValue(String.class));

                    tvPhone.setText(dataSnapshot.child("phone").getValue(String.class));

                    tvAddress.setText(dataSnapshot.child("country").getValue(String.class)
                            +" "+dataSnapshot.child("state").getValue(String.class)
                            +" "+dataSnapshot.child("homeAddress").getValue(String.class)

                    );

                    tvFriendCount.setText(tvFriendCount.getText());

                    if(dataSnapshot.child("pp_path").getValue().toString().equals("")){
                        profilePic.setImageResource(R.drawable.ic_profile_male);
                    }
                    else{
                        Picasso.get().load(dataSnapshot.child("pp_path").getValue().toString()).into(profilePic);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void loadDataIntoControls(){

    }

    @Override
    public boolean validateFields() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnImgUpload:
                // start picker to get image for cropping and then use the image in cropping activity
                        CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;

            default:
                    break;



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                firebaseDAO.getStorageReference()
                        .child("profile_pictures")
                        .child(user.getUserID())
                        .child(user.getUserID())
                        .putFile(result.getUri())
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            //if uploading to firebase storage is successfull
                            if(task.isSuccessful()) {

                                //if download URL for the image uploaded has been generated
                                firebaseDAO.getStorageReference()
                                        .child("profile_pictures")
                                        .child(user.getUserID())
                                        .child(user.getUserID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        //updating image path in user's pp_path attribute
                                        firebaseDAO.getDbReference()
                                                .child("users")
                                                .child(user.getUserID())
                                                .child("pp_path").setValue(uri.toString());

                                        Picasso.get().load(uri.toString()).into(profilePic);
                                    }
                                });
                            }
                            else {
                                Toast.makeText(Profile.this,"some error occured.please try again later",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
