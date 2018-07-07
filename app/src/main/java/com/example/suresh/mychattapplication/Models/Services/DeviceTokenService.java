package com.example.suresh.mychattapplication.Models.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class DeviceTokenService extends FirebaseInstanceIdService {

    private String refreshedToken;

    public DeviceTokenService()
    {
        onTokenRefresh();
    }
    @Override
    public void onTokenRefresh() {

        refreshedToken= FirebaseInstanceId.getInstance().getToken();

    }

    public String getRefreshedToken() {
        return refreshedToken;
    }
}
