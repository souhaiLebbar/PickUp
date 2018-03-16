package com.example.rustybucket.pickup;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by adamchen on 2/20/18.
 */
public class UserTest {
    // User testCase = new User("Adam", "5410000000", "axxx@uoregon.edu", "I wanna do some funny tests", "uuid is what?", notifications, zipCodes);
    @Test
    public void test_getAndSetUserName() throws Exception {
        User testCase = new User();
        testCase.setUserName("Adam");
        assertEquals(true, testCase.getUserName().equals("Adam"));
        testCase.setUserName("Chen");
        assertEquals(true, testCase.getUserName().equals("Chen"));
    }

    @Test
    public void test_getAndSetPhoneNumber() throws Exception {
        User testCase = new User();
        testCase.setPhoneNumber("541-000-0000");
        assertEquals(true, testCase.getPhoneNumber().equals("541-000-0000"));
        testCase.setPhoneNumber("541-000-0001");
        assertEquals(true, testCase.getPhoneNumber().equals("541-000-0001"));
    }

    @Test
    public void test_getEmailAddress() throws Exception {
        List<String> notifications = new LinkedList<>();
        List<String> zipCodes = new LinkedList<>();
        User testCase = new User("Adam", "5410000000", "axxx@uoregon.edu", "I wanna do some funny tests", "uuid is what?", notifications, zipCodes, "TEST token");
        assertEquals(true, testCase.getEmailAddress().equals("axxx@uoregon.edu"));
    }

    @Test
    public void test_getAndSetDescription() throws Exception {
        User testCase = new User();
        testCase.setDescription("Test is funny, isn't it?");
        assertEquals(true, testCase.getDescription().equals("Test is funny, isn't it?"));
        testCase.setDescription("No, it is not");
        assertEquals(true, testCase.getDescription().equals("No, it is not"));
    }

    @Test
    public void test_getUuid() throws Exception {
        List<String> notifications = new LinkedList<>();
        List<String> zipCodes = new LinkedList<>();
        User testCase = new User("Adam", "5410000000", "axxx@uoregon.edu", "I wanna do some funny tests", "uuid is what?", notifications, zipCodes, "TEST token");
        assertEquals(true, testCase.getUuid().equals("uuid is what?"));
    }

    @Test
    public void test_getAndSetNotificationIDs() throws Exception {
        List<String> notifications = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("0x000x").append(i);
            notifications.add(sb.toString());
        }
        User testCase = new User();
        testCase.setNotificationIds(notifications);
        List<String> testList = testCase.getNotificationIds();
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("0x000x").append(i);
            assertEquals(true, testList.get(i).equals(sb.toString()));
        }
    }

    @Test
    public void test_getAndSetZipCodes() throws Exception {
        List<String> zipCodes = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("9740").append(i);
            zipCodes.add(sb.toString());
        }
        User testCase = new User();
        testCase.setZipCodes(zipCodes);
        List<String> testList = testCase.getZipCodes();
        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("9740").append(i);
            assertEquals(true, testList.get(i).equals(sb.toString()));
        }
    }

    @Test
    public void test_getAndSetNotificationToken() throws Exception {
        User testCase = new User();
        testCase.setNotificationToken("Test is funny, isn't it?");
        assertEquals(true, testCase.getNotificationToken().equals("Test is funny, isn't it?"));
        testCase.setNotificationToken("No, it is not");
        assertEquals(true, testCase.getNotificationToken().equals("No, it is not"));
    }


}