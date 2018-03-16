package com.example.rustybucket.pickup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String DATABASE_USER_LIST = "users";

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private static final String TAG = "MyFirebaseIDService";
    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshToken);


        sendRegistrationToServer(refreshToken);
    }

    private void sendRegistrationToServer(String token) {
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //String id = user.getUid();
        //database.child(DATABASE_USER_LIST).child(id).child("notificationToken").setValue(token);


    }
}
