package com.example.rustybucket.pickup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 2/15/2018.
 */

public class DataManager {

    // Authentication object
    private FirebaseAuth mAuth;

    // Database specific constants
    private static final String DATABASE_USER_LIST = "users";
    private static final String ZIPCODE_USERID_REFERENCE_LIST = "userIDs";
    private static final String DATABASE_NOTIFICATION_LIST = "notifications";
    private static final String ZIPCODE_NOTIFICATIONS_REFERENCE_LIST = "notificationIDs";
    private static final String NOTIFICATIONS_UUID = "uuid";
    private static final String ZIPCODE_NOTIFICATION_TOKENS_LIST = "notificationTokens";

    // Firebase specific constant references
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference baseRef;
    private DatabaseReference userRef;
    private DatabaseReference notificationRef;

    // If user exists in database
    public Boolean EXISTS;

    public DataManager() {
        mAuth = FirebaseAuth.getInstance();
        baseRef = database.getReference();
        userRef = baseRef.child(DATABASE_USER_LIST);
        notificationRef = baseRef.child(DATABASE_NOTIFICATION_LIST);
    }

    /* HOWTO use:
     * DataManager dataManager = new DataManager();
     * dataManager.getUser(testUser.getUuid()).addOnSuccessListener(new OnSuccessListener<User>() {
     *      @Override
     *      public void onSuccess(User user) {
     *
     *      }
     *  });
     */
    public Task<User> getUser(String uuid)  {
        // Starts an asynchronous task to get user from database
        // caller should call an on success listener to get the user with associated uuid

        final TaskCompletionSource<User> tcs = new TaskCompletionSource<>();
        userRef.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tcs.setResult(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tcs.getTask();
    }

    /* HOWTO use:
     * DataManager dataManager = new DataManager();
     * dataManager.getUsers("97403").addOnSuccessListener(new OnSuccessListener<List<User>>() {
     *      @Override
     *      public void onSuccess(List<User> users) {
     *
     *      }
     *  });
     */
    public Task<List<User>> getUsers(String zipCode)  {
        // Starts an asynchronous task to get user from database
        // caller should call an on success listener to get all users under the specified zipcode

        final TaskCompletionSource<List<User>> tcs = new TaskCompletionSource<>();
        baseRef.child(zipCode).child(ZIPCODE_USERID_REFERENCE_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                final List<String> userReferences = new LinkedList<>(dataSnapshot.getValue(t).keySet());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> result = new LinkedList<>();
                        GenericTypeIndicator<HashMap<String, User>> t = new GenericTypeIndicator<HashMap<String, User>>() {};
                        HashMap<String, User> allUsers = new HashMap<>(dataSnapshot.getValue(t));
                        for (String userReference : userReferences) {
                            if (allUsers.get(userReference) != null) {
                                result.add(allUsers.get(userReference));
                            }
                        }
                        tcs.setResult(result);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return tcs.getTask();
    }

    /* HOWTO use:
     * DataManager dataManager = new DataManager();
     * dataManager.getAllUsers().addOnSuccessListener(new OnSuccessListener<List<User>>() {
     *      @Override
     *      public void onSuccess(List<User> users) {
     *
     *      }
     *  });
     */
    public Task<List<User>> getAllUsers()  {
        // Starts an asynchronous task to get user from database
        // caller should call an on success listener to get all the users stored

        final TaskCompletionSource<List<User>> tcs = new TaskCompletionSource<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, User>> t = new GenericTypeIndicator<HashMap<String, User>>() {};
                List<User> result = new LinkedList<>(dataSnapshot.getValue(t).values());
                tcs.setResult(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tcs.getTask();
    }

    public Task<Notification> getNotification(String uuid) {
        // Starts an asynchronous task to get notification from database
        // caller should call an on success listener to get the notification with the associated uuid

        final TaskCompletionSource<Notification> tcs = new TaskCompletionSource<>();
        notificationRef.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tcs.setResult(dataSnapshot.getValue(Notification.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tcs.getTask();
    }

    public Task<List<Notification>> getNotifications(String zipCode) {
        // Starts an asynchronous task to get notifications by specific zipcode from database
        // caller should call an on success listener to get all the notifications under the specified zipcode

        final TaskCompletionSource<List<Notification>> tcs = new TaskCompletionSource<>();
        baseRef.child(zipCode).child(ZIPCODE_NOTIFICATIONS_REFERENCE_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                if(dataSnapshot == null){
                    tcs.setResult(new LinkedList<Notification>());
                    return;
                } else if(dataSnapshot.getValue(t) == null){
                    tcs.setResult(new LinkedList<Notification>());
                    return;
                }
                final List<String> notificationReferences = new LinkedList<>(dataSnapshot.getValue(t).keySet());
                notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Notification> result = new LinkedList<>();
                        GenericTypeIndicator<HashMap<String, Notification>> t = new GenericTypeIndicator<HashMap<String, Notification>>() {};
                        HashMap<String, Notification> allNotifications = new HashMap<>(dataSnapshot.getValue(t));
                        for (String nReference : notificationReferences) {
                            if (allNotifications.get(nReference) != null) {
                                result.add(allNotifications.get(nReference));
                            }
                        }
                        tcs.setResult(result);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return tcs.getTask();
    }

    public Task<List<Notification>> getAllNotifications() {
        // Starts an asynchronous task to get all notifications from database
        // caller should call an on success listener to get all notifications stored

        final TaskCompletionSource<List<Notification>> tcs = new TaskCompletionSource<>();
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Notification>> t = new GenericTypeIndicator<HashMap<String, Notification>>() {};
                List<Notification> result = new LinkedList<>(dataSnapshot.getValue(t).values());
                tcs.setResult(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tcs.getTask();
    }

    public Task<Map<String, Notification>> getAllNotificationsMap() {
        // Starts an asynchronous task to get all notifications from database
        // caller should call an on success listener to get all notifications stored

        final TaskCompletionSource<Map<String, Notification>> tcs = new TaskCompletionSource<>();
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Notification>> t = new GenericTypeIndicator<HashMap<String, Notification>>() {
                };
                Map<String, Notification> result = new HashMap<>(dataSnapshot.getValue(t));
                tcs.setResult(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return tcs.getTask();
    }

    // TODO: make a working version haha
    public Task<List<Notification>> getNotificationsByIds(List<String> notifIds) {
        // Starts an asynchronous task to get notifications from database
        // caller should call an on success listener to get all users under the specified zipcode

        final TaskCompletionSource<List<Notification>> tcs = new TaskCompletionSource<>();
        final List<Notification> notificationList = new LinkedList<>();


        for (int i = 0; i < notifIds.size(); i++){
            String id = notifIds.get(i);
            System.out.println(id + " LLLLLLLLLLLL");
            getNotification(id).addOnSuccessListener(new OnSuccessListener<Notification>() {
                @Override
                public void onSuccess(Notification notification) {
                    System.out.println("SUCCESS ADDING " + notification);
                    notificationList.add(notification);
                }
            });

        }
        tcs.setResult(notificationList);

        System.out.println("b4 RETURN " + notificationList);
        return tcs.getTask();
    }

    public void addUser(User u){
        // Adds user to user list
        // Adds user's reference ID to corresponding zip code list

        userRef.child(u.getUuid()).setValue(u);

        DatabaseReference zipUserReference;
        DatabaseReference zipNotifTokenReference;
        List<String> zipList = u.getZipCodes();

        for (String zipCode : zipList) {
            zipUserReference = baseRef.child(zipCode).child(ZIPCODE_USERID_REFERENCE_LIST);
            // Add uuid to zipcode user list
            zipUserReference.child(u.getUuid()).setValue(u.getUuid());

            // Add notifToken to list
            zipNotifTokenReference = baseRef.child(zipCode).child(ZIPCODE_NOTIFICATION_TOKENS_LIST);
            zipNotifTokenReference.child(u.getNotificationToken()).setValue(u.getNotificationToken());

        }
    }

    public void addNotification(String zipCode, Notification n){
        // Appends new notification to notification list
        // Assigns notification reference to zipcode
        // Updates the creator that this notification was created by

        User u = n.getCreator();

        notificationRef.child(n.getUuid()).setValue(n);
        notificationRef.child(n.getUuid()).child(NOTIFICATIONS_UUID).setValue(n.getUuid());

        DatabaseReference zipNotificationRef = baseRef.child(zipCode).child(ZIPCODE_NOTIFICATIONS_REFERENCE_LIST);
        zipNotificationRef.child(n.getUuid()).setValue(n.getUuid());

        userRef.child(u.getUuid()).child(n.getUuid());
        if ((u.getNotificationIds() == null) || !(u.getNotificationIds().contains(n.getUuid()))) {
            u.addNotificationIds(n);
        }
        userRef.child(u.getUuid()).setValue(u);
    }

    public Task<String> getIndexOfNotification(String uuid, final String notifId){
        final TaskCompletionSource<String> tcs = new TaskCompletionSource<>();
        userRef.child(uuid).child("notificationIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    String id = postSnapshot.getValue(String.class);
                    if (id.equals(notifId)){
                        tcs.setResult(key);
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return tcs.getTask();
    }

    public void deleteNotification(String zipCode, String notificationId){

        // Remove notification from 'notifications' node
        notificationRef.child(notificationId).removeValue();

        // Remove notification id from '{zipCode}' node
        baseRef.child(zipCode).child(notificationId).removeValue();

        // Get user and remove from their list
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uuid = user.getUid();
        getIndexOfNotification(uuid, notificationId).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                userRef.child("notificationIds").child(s).removeValue();
            }
        });
    }

    public void editNotification(String notificationId, String description, Date dateAndTime,
                                 List<String> tags, String activityName){
        // Retrieve notification reference
        DatabaseReference notification = notificationRef.child(notificationId);

        // Edit description
        notification.child("description").setValue(description);

        // Edit date
        notification.child("time").setValue(dateAndTime);

        // Edit tags
        notification.child("tags").setValue(tags);

        // Edit activity name
        notification.child("activityName").setValue(activityName);
    }

    public void editProfile(String uuid, String description){
        // Retrieve notification reference
        DatabaseReference user = userRef.child(uuid);

        // Edit description
        user.child("description").setValue(description);

    }

    public Task<Boolean> ifUserExists(final String uuid){
        // Declared final for nested reference issues
        final String userID = uuid;

        // tcs is what we return
        final TaskCompletionSource<Boolean> tcs = new TaskCompletionSource<>();

        // Checks if a user is in the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loop through 'users' node and check each leaf
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    // If the uuid exists in our 'users' node in the database
                    if(data.getKey().equals(uuid)){
                        // Set TaskCompleteSource result to true
                        tcs.setResult(true);
                        return;
                    }
                }
                // Could not find uuid in the 'users' node in the database
                // Set TCS to false
                tcs.setResult(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return tcs.getTask();
    }
}
