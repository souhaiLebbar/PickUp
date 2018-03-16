package com.example.rustybucket.pickup;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNotification extends DialogFragment {

    TextView activity_name;
    TextView time;
    TextView location;
    TextView zipcode;
    TextView description;
    TextView creator;

    public ViewNotification() {
        // Required empty public constructor
    }

    public static ViewNotification newInstance(String uuid) {
        ViewNotification frag = new ViewNotification();
        Bundle args = new Bundle();
        args.putString("uuid", uuid);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_view_notification, container, false);

        activity_name = rootView.findViewById(R.id.activity_name);
        time = rootView.findViewById(R.id.activity_time);
        location = rootView.findViewById(R.id.activity_location);
        zipcode = rootView.findViewById(R.id.activity_zipcodes);
        description = rootView.findViewById(R.id.activity_description);
        creator = rootView.findViewById(R.id.activity_creator);

        String uuid = getArguments().getString("uuid");

        DataManager dataManager = new DataManager();
        dataManager.getNotification(uuid).addOnSuccessListener(new OnSuccessListener<Notification>() {
            @Override
            public void onSuccess(Notification notification) {
                activity_name.setText(notification.getActivityName());
                time.setText(notification.getTime().toString());
                zipcode.setText(notification.getZipCodes().toString());
                description.setText(notification.getDescription());
                creator.setText(notification.getCreator().getUserName());
            }
        });



        return rootView;
    }

}
