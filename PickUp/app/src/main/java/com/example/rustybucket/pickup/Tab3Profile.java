package com.example.rustybucket.pickup;

/**
 * Created by Jeffrey on 2/15/2018.
 */
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class Tab3Profile extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1profile, container, false);


        final TextView username = rootView.findViewById(R.id.user_profile_name);

        final TextView email = rootView.findViewById(R.id.user_profile_email);

        final TextView description = rootView.findViewById(R.id.user_profile_description);

        final TextView zipcode = rootView.findViewById(R.id.user_profile_zipcode);


        DataManager dataManager = new DataManager();
        dataManager.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                username.setText(user.getUserName());
                email.setText(user.getEmailAddress());
                List<String> zips = user.getZipCodes();
                zipcode.setText(zips.get(0));
                if (zips.size() >= 1) {
                    for (int i = 1; i < zips.size(); ++i) {
                        zipcode.setText(zipcode.getText().toString() + ", " + zips.get(i));
                    }
                }
                description.setText(user.getDescription());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.editProfileButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getContext(), EditProfileActivity.class);
                myIntent.putExtra("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(myIntent);
                //getActivity().finish();
            }
        });

        return rootView;
    }

}
