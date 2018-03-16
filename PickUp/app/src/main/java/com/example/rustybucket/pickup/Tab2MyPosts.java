package com.example.rustybucket.pickup;

/**
 * Created by Jeffrey on 2/15/2018.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class Tab2MyPosts extends Fragment{

    DataManager dataManager = new DataManager();
    LinkedList<String> notificationIds;
    volatile boolean done = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        View rootView = inflater.inflate(R.layout.tab3notifications, container, false);

        // GUI element that this is being applied to
        ListView listView = (ListView) rootView.findViewById(R.id.notificationList);

        activity.getOwnNotifications(rootView, activity, listView);

        return rootView;

    }
}
