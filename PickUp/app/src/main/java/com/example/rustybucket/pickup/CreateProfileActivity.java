package com.example.rustybucket.pickup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CreateProfileActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private FirebaseUser curUser;

    private LocManager locManager;

    private EditText userName;
    private EditText location;
    private TextView zipCode;
    private EditText description;
    private EditText phoneNumber;

    private Toolbar toolbar;

    public String uuid;
    private List<String> zipList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile2);

        locManager = new LocManager(CreateProfileActivity.this);
        locManager.getZipcodeAtCurrentLocation().addOnSuccessListener(new OnSuccessListener<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                zipList = strings;
            }
        });

        curUser = FirebaseAuth.getInstance().getCurrentUser();

        // User info from form
        userName = findViewById(R.id.userEntry);
        location =  findViewById(R.id.locationEntry);
        phoneNumber = findViewById(R.id.phoneEntry);
        zipCode = findViewById(R.id.zipCodeEntry);
        description = findViewById(R.id.descriptionEntry);
        uuid = curUser.getUid();

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cancelProfile) {
            this.finish();
            return true;
        }
        if (id == R.id.confirmProfile) {
            boolean descriptionEmptyFlag = description.getText().toString().isEmpty();
            boolean usernameEmptyFlag = userName.getText().toString().isEmpty();
            boolean phoneNumberEmptyFlag = phoneNumber.getText().toString().isEmpty();
            if (descriptionEmptyFlag || usernameEmptyFlag || phoneNumberEmptyFlag) {
                Toast toast=Toast.makeText(getApplicationContext(),"All fields besides location are required",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {

                //set values
                this.finish();

                createProfile();
                return true;
            }
        }
        return true;
    }

    private void createProfile() {
        /* TODO
          - add checks for invalid inputs
          - do not go to next activity, display error for invalid inputs
        */

        // Initialize user class with info from form
        MyFirebaseInstanceIDService mfi = new MyFirebaseInstanceIDService();
        mfi.onTokenRefresh();
        String notifToken = FirebaseInstanceId.getInstance().getToken();

        if(zipList == null || zipList.size() == 0){
            zipList = new LinkedList<>(Arrays.asList(zipCode.getText().toString()));
        }

        User newUser = new User(userName.getText().toString(), phoneNumber.getText().toString(),
                curUser.getEmail(), description.getText().toString(), curUser.getUid(),
                new LinkedList<String>(), new LinkedList<>(zipList), notifToken);

        // Add user with DataManager class
        DataManager dataManager = new DataManager();
        dataManager.addUser(newUser);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userUuid", newUser.getUuid());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == Constants.PERMISSION_REQUEST_FINE_LOCATION) {
            locManager = new LocManager(CreateProfileActivity.this);
            locManager.getZipcodeAtCurrentLocation().addOnSuccessListener(new OnSuccessListener<List<String>>() {
                @Override
                public void onSuccess(List<String> strings) {
                    zipList = strings;
                }
            });
        } else {
            //TODO Make a toast or snackbar message to describe how this app will not function well without this permission allowed
            zipList = new LinkedList<>(Arrays.asList(zipCode.getText().toString()));
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

}
