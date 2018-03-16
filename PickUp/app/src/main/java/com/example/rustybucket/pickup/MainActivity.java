package com.example.rustybucket.pickup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public User currentUser;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    DataManager dataManager = new DataManager();


    private GoogleSignInClient gsic;

    final String firstLine = "First Line", secondLine = "Second Line", thirdLine = "Third Line", uuid = "uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String uuid = getIntent().getStringExtra("userUuid");

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DataManager dataManager = new DataManager();
        dataManager.getUser(uuid).addOnSuccessListener(new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
                        startActivity(myIntent);
                    }
                });



            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsic = GoogleSignIn.getClient(this, gso);
        String token = FirebaseInstanceId.getInstance().getToken();
        MyFirebaseInstanceIDService mfi = new MyFirebaseInstanceIDService();
        mfi.onTokenRefresh();


    }

    private void signOut(){
        gsic.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("User Signed Out");
                FirebaseAuth.getInstance().signOut();
                Intent signOutIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signOutIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getNotificationData(View rootView, final Context activity, ListView listView) {

        final List<HashMap<String, String>> notificationsList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(activity, notificationsList, R.layout.notification_item,
                new String[]{firstLine, secondLine, thirdLine},
                new int[]{R.id.text1, R.id.text2, R.id.text3});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getSupportFragmentManager();
                ViewNotification viewNotification = ViewNotification.newInstance(notificationsList.get(i).get("uuid"));
                viewNotification.show(fm, "activity_view_notification");
            }
        });

        final DateFormat df = new SimpleDateFormat("EEE MMM dd h:m:s z y");
        List<String> zipList = currentUser.getZipCodes();
        for (String zip : zipList) {
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child(zip).child("notificationIDs");
            dref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final HashMap<String, String> resultsMap = new HashMap<>();
                    final String n = dataSnapshot.getValue(String.class);
                    dataManager.getAllNotificationsMap().addOnSuccessListener(new OnSuccessListener<Map<String, Notification>>() {
                        @Override
                        public void onSuccess(final Map<String, Notification> notifications) {
                            if (notifications.get(n).getTime().after(new Date())) {
                                resultsMap.put(uuid, notifications.get(n).getUuid());
                                resultsMap.put(firstLine, notifications.get(n).getActivityName());
                                resultsMap.put(secondLine, notifications.get(n).getTime().toString());
                                resultsMap.put(thirdLine, notifications.get(n).getDescription());
                                Date curTime = notifications.get(n).getTime();
                                boolean added = false;
                                for (int i = 0; i < notificationsList.size(); ++i) {
                                    try {
                                        if (curTime.before(df.parse(notificationsList.get(i).get(secondLine)))) {
                                            notificationsList.add(i,resultsMap);
                                            added = true;
                                            break;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!added) {
                                    notificationsList.add(resultsMap);
                                }

                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

            });
        }
    }



    public void getOwnNotifications(View rootView, final Context activity, ListView listView) {

        final List<HashMap<String, String>> notificationsList = new LinkedList<>();
        final SimpleAdapter adapter = new SimpleAdapter(activity, notificationsList, R.layout.notification_item,
                new String[]{firstLine, secondLine, thirdLine},
                new int[]{R.id.text1, R.id.text2, R.id.text3});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getSupportFragmentManager();
                ViewNotification viewNotification = ViewNotification.newInstance(notificationsList.get(i).get("uuid"));
                viewNotification.show(fm, "activity_view_notification");
            }
        });

        final DateFormat df = new SimpleDateFormat("EEE MMM dd h:m:s z y");
        List<String> zipList = currentUser.getZipCodes();
        for (String zip : zipList) {
            DatabaseReference userNotifRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUuid()).child("notificationIds");
            final DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child(zip).child("notificationIDs");
            userNotifRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final HashMap<String, String> resultsMap = new HashMap<>();
                    final String n = dataSnapshot.getValue(String.class);
                    dataManager.getAllNotificationsMap().addOnSuccessListener(new OnSuccessListener<Map<String, Notification>>() {
                        @Override
                        public void onSuccess(final Map<String, Notification> notifications) {
                            if (notifications.get(n).getTime().after(new Date())) {
                                resultsMap.put(uuid, notifications.get(n).getUuid());
                                resultsMap.put(firstLine, notifications.get(n).getActivityName());
                                resultsMap.put(secondLine, notifications.get(n).getTime().toString());
                                resultsMap.put(thirdLine, notifications.get(n).getDescription());
                                Date curTime = notifications.get(n).getTime();
                                boolean added = false;
                                for (int i = 0; i < notificationsList.size(); ++i) {
                                    try {
                                        if (curTime.before(df.parse(notificationsList.get(i).get(secondLine)))) {
                                            notificationsList.add(i,resultsMap);
                                            added = true;
                                            break;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (!added) {
                                    notificationsList.add(resultsMap);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Returning the current tabs
            switch (position) {
                case 0:
                    Tab1Notifications tab1 = new Tab1Notifications();
                    return tab1;
                case 1:
                    Tab2MyPosts tab2 = new Tab2MyPosts();
                    return tab2;
                case 2:
                    Tab3Profile tab3 = new Tab3Profile();
                    return  tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    protected void displayMemoryUsage(String message) {
        int usedKBytes = (int) (Debug.getNativeHeapAllocatedSize() / 1024L);
        String usedMegsString = String.format("%s - usedMemory = Memory Used: %d KB", message, usedKBytes);
        Log.d("TAG", usedMegsString);
    }
}
