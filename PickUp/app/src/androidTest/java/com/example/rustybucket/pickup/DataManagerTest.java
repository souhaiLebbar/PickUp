package com.example.rustybucket.pickup;

import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by adamchen on 2/26/18.
 */
public class DataManagerTest {
    @Test
    public void getAllUsers() throws Exception {
        DataManager dm = new DataManager();
        final CompletableFuture<List<User>> future = new CompletableFuture<>();
        dm.getAllUsers().addOnSuccessListener(new OnSuccessListener<List<User>>() {
            @Override
            public void onSuccess(List<User> res) {
                future.complete(res);
            }
        });
        List<User> temp = future.get();
        /*
        * It may be not a good way to test List<User> we got
        * */
        // we have 5 normal users and one extra test user
        /* TODO find a good way to test it*/
    }


    @Test
    public void getAllNotifications() throws Exception {
        DataManager dm = new DataManager();
        final CompletableFuture<List<Notification>> future = new CompletableFuture<>();
        dm.getAllNotifications().addOnSuccessListener(new OnSuccessListener<List<Notification>>() {
            @Override
            public void onSuccess(List<Notification> n) {
                future.complete(n);
            }
        });
        List<Notification> temp =future.get();
        /* TODO find a good way to test it*/
    }

    @Test
    public void test_addAndGetUser() throws Exception {
        /* Add User */
        List<String> notifications = new LinkedList<>();
        List<String> zipCodes = new LinkedList<>();
        zipCodes.add("Test 97401");
        User testCase = new User("Adam", "5410000000", "axxx@uoregon.edu", "I wanna do some funny tests", "TEST UUID 1", notifications, zipCodes, "TEST token");
        DataManager dm = new DataManager();
        dm.addUser(testCase);

        /* Get User */
        final CompletableFuture<User> future = new CompletableFuture<>();
        dm.getUser("Test USER UUID 1").addOnSuccessListener(new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User u) {
                future.complete(u);
            }
        });
        User temp = future.get();
        assertEquals(true, temp.getPhoneNumber().equals("5410000000"));
        assertEquals(true, temp.getUserName().equals("Adam"));
        assertEquals(true, temp.getDescription().equals("I wanna do some funny tests"));
        assertEquals(true, temp.getEmailAddress().equals("axxx@uoregon.edu"));
        assertEquals(true, temp.getZipCodes().size() == 1);
        assertEquals(true, temp.getZipCodes().get(0).equals("Test 97401"));
        assertEquals(true, temp.getNotificationToken().equals("TEST token"));
    }

    @Test
    public void getUsers() throws Exception {
        DataManager dm = new DataManager();
        final CompletableFuture<List<User>> future = new CompletableFuture<>();
        dm.getUsers("Test 97401").addOnSuccessListener(new OnSuccessListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                future.complete(users);
            }
        });
        // in our case, we only have one test user under "Test 97401"
        List<User> temp = future.get();
        assertEquals(1, temp.size());
        assertEquals(true, temp.get(0).getPhoneNumber().equals("5410000000"));
        assertEquals(true, temp.get(0).getUserName().equals("Adam"));
        assertEquals(true, temp.get(0).getDescription().equals("I wanna do some funny tests"));
        assertEquals(true, temp.get(0).getEmailAddress().equals("axxx@uoregon.edu"));
        assertEquals(true, temp.get(0).getZipCodes().size() == 1);
        assertEquals(true, temp.get(0).getZipCodes().get(0).equals("Test 97401"));
    }

    @Test
    public void addAndGetNotification() throws Exception {
        /* Add Notification */
        DataManager dm = new DataManager();
        List<String> notifications = new LinkedList<>();
        List<String> zipCodes = new LinkedList<>();
        User user = new User("Adam", "5410000000", "axxx@uoregon.edu", "I wanna do some funny tests", "TEST UUID 2", notifications, zipCodes, "TEST token");
        Date date = new Date(2018, 02, 22);
        List<String> tags = new ArrayList<>();
        List<String> zipcodes = new ArrayList<>();
        tags.add("OverWatch");
        tags.add("CSGO");
        String activity = "Test Activity";
        Notification testCase= new Notification(user, "Play overwatch please", date, "TEST UUID", tags, activity, zipcodes);
        dm.addNotification("Test 97401", testCase);
        /* Get Notification */
        final CompletableFuture<Notification> future = new CompletableFuture<>();
        dm.getNotification("Test Notification UUID 1").addOnSuccessListener(new OnSuccessListener<Notification>() {
            @Override
            public void onSuccess(Notification n) {
                future.complete(n);
            }
        });
        Notification temp = future.get();
        assertEquals(true, temp.getDescription().equals("Play overwatch please"));
        assertEquals(true, temp.getUuid().equals("Test Notification UUID 1"));
        assertEquals(true, temp.getTime().getMonth() == 2);
        assertEquals(true, temp.getTime().getYear() == 2018);
        assertEquals(true, temp.getTime().getDate() == 22);
    }

    @Test
    public void getNotifications() throws Exception {
        DataManager dm = new DataManager();
        final CompletableFuture<List<Notification>> future = new CompletableFuture<>();
        dm.getNotifications("Test 97401").addOnSuccessListener(new OnSuccessListener<List<Notification>>() {
            @Override
            public void onSuccess(List<Notification> n) {
                future.complete(n);
            }
        });
        List<Notification> temp =future.get();
        assertEquals(true, temp.get(0).getDescription().equals("Play overwatch please"));
        assertEquals(true, temp.get(0).getUuid().equals("Test Notification UUID 1"));
        assertEquals(true, temp.get(0).getTime().getMonth() == 2);
        assertEquals(true, temp.get(0).getTime().getYear() == 2018);
        assertEquals(true, temp.get(0).getTime().getDate() == 22);
    }


    @Test
    public void checkZip() throws Exception {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void test_ifUserExists() throws ExecutionException, InterruptedException {
        DataManager dm = new DataManager();
        final CompletableFuture<Boolean> future1 = new CompletableFuture<>();
        dm.ifUserExists("aaaa").addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                future1.complete(aBoolean);
            }
        });
        assertEquals(false, future1.get());

        final CompletableFuture<Boolean> future2 = new CompletableFuture<>();
        dm.ifUserExists("VPVMZ1TeEESKOlASkDHBOJPQt6o2").addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                future2.complete(aBoolean);
            }
        });
        assertEquals(true, future2.get());
    }

    @Test
    public void getAllNotificationsMap() throws ExecutionException, InterruptedException {
        DataManager dm = new DataManager();
        final CompletableFuture<Map<String, Notification>> future = new CompletableFuture<>();
        dm.getAllNotificationsMap().addOnSuccessListener(new OnSuccessListener<Map<String, Notification>>() {
            @Override
            public void onSuccess(Map<String, Notification> map) {
                future.complete(map);
            }
        });



    }

}