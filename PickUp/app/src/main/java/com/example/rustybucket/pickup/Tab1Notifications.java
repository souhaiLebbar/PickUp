package com.example.rustybucket.pickup;

/**
 * Created by Jeffrey on 2/15/2018.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class Tab1Notifications extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View rootView = inflater.inflate(R.layout.tab3notifications, container, false);

        // GUI element that this is being applied to
        ListView listView = (ListView) rootView.findViewById(R.id.notificationList);

        activity.getNotificationData(rootView, activity, listView);

        return rootView;
    }
}
