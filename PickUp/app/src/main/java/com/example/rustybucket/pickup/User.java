package com.example.rustybucket.pickup;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 2/13/2018.
 */

public class User {
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private String description;
    private String uuid;
    private List<String> notificationIds;
    private List<String> zipCodes;
    private String notificationToken;


    public User(){

    }

    public User(String userName, String phoneNumber, String emailAddress, String description,
                String uuid, List<String> notificationIds, List<String> zipCodes, String notifcationToken){
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.description = description;
        this.uuid = uuid;
        this.notificationIds = notificationIds;
        this.zipCodes = zipCodes;
        this.notificationToken = notifcationToken;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String UserName){
        this.userName = UserName;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress(){
        return this.emailAddress;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getUuid(){
        return this.uuid;
    }

    public List<String> getNotificationIds(){
        return this.notificationIds;
    }

    public void setNotificationIds(List<String> notificationIds){
        this.notificationIds = notificationIds;
    }

    public void addNotificationIds(Notification n) {
        if (notificationIds == null) {
            notificationIds = new LinkedList<>();
        }
        this.notificationIds.add(n.getUuid());
    }

    public List<String> getZipCodes(){
        return this.zipCodes;
    }

    public void setZipCodes(List<String> ZipCodes) {
        this.zipCodes = ZipCodes;
    }

    public String getNotificationToken(){ return this.notificationToken; }

    public void setNotificationToken(String notifcationToken){ this.notificationToken = notifcationToken; }

}
