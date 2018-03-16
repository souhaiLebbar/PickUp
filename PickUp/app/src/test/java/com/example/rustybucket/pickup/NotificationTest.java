package com.example.rustybucket.pickup;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by adamchen on 2/22/18.
 */
public class NotificationTest {
    @Test
    public void test_getCreator() throws Exception {
        User user = new User();
        user.setUserName("Adam");
        Date date = new Date(2018, 02, 22);
        List<String> tags = new ArrayList<>();
        List<String> zipcodes = new ArrayList<>();
        tags.add("OverWatch");
        tags.add("CSGO");
        String activity = "Test Activity";
        Notification testCase= new Notification(user, "Play overwatch please", date, "TEST UUID", tags, activity, zipcodes);
        User temp = testCase.getCreator();
        assertEquals(true, temp.getUserName().equals("Adam"));
    }

    @Test
    public void test_setAndGetDescription() throws Exception {
        Notification testCase = new Notification();
        testCase.setDescription("I am happy");
        assertEquals(true, testCase.getDescription().equals("I am happy"));
    }

    @Test
    public void test_setAndGetTime() throws Exception {
        Date date = new Date(2018, 02, 22);
        Notification testCase = new Notification();
        testCase.setTime(date);
        Date temp = testCase.getTime();

        assertEquals(true, temp.getMonth() == 2);
        assertEquals(true, temp.getYear() == 2018);
        assertEquals(true, temp.getDate() == 22);
    }


    @Test
    public void test_getUuid() throws Exception {
        User user = new User();
        Date date = new Date(2018, 02, 22);
        List<String> tags = new ArrayList<>();
        tags.add("OverWatch");
        tags.add("CSGO");
        List<String> zipcodes = new ArrayList<>();
        String activity = "Test Activity";
        Notification testCase= new Notification(user, "Play overwatch please", date, "TEST UUID", tags, activity, zipcodes);
        assertEquals(true, testCase.getUuid().equals("TEST UUID"));
    }

    @Test
    public void test_setAndSetTags() throws Exception {
        Notification testCase = new Notification();
        List<String> tags = new ArrayList<>();
        tags.add("OverWatch");
        tags.add("CSGO");
        testCase.setTags(tags);
        List<String> temp = testCase.getTags();
        for (int i = 0; i < tags.size(); i++) {
            assertEquals(true, temp.get(i).equals(tags.get(i)));
        }
    }

    @Test
    public void test_setAndSetActivityName() throws Exception {
        Notification testCase = new Notification();
        testCase.setActivityName("Test Game");
        assertEquals(true, testCase.getActivityName().equals("Test Game"));
    }

    @Test
    public void test_setAndSetZipCodes() throws Exception {
        Notification testCase = new Notification();
        List<String> zips = new ArrayList<>();
        zips.add("TEST 97401");
        zips.add("TEST 97402");
        testCase.setZipCodes(zips);
        List<String> temp = testCase.getZipCodes();
        for (int i = 0; i < zips.size(); i++) {
            assertEquals(true, temp.get(i).equals(zips.get(i)));
        }
    }

}