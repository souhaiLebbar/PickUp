package com.example.rustybucket.pickup;

import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Created by Erik on 2/11/2018.
 */

public class Notification {
    private User creator;
    private String description;
    private Date dateAndTime;
    private String uuid;
    private List<String> tags;
    private String activityName;
    private List<String> zipCodes;

    public Notification(){

    }

    public Notification(User creator, String description, Date dateAndTime, List<String> tags,
                        String activityName, List<String> zipCodes){
        this.creator = creator;
        this.description = description;
        this.dateAndTime = dateAndTime;
        this.uuid = FirebaseDatabase.getInstance().getReference().child("notifications").push().getKey();
        this.tags = tags;
        this.activityName = activityName;
        this.zipCodes = zipCodes;
    }

    public Notification(User creator, String description, Date dateAndTime, String uuid, List<String> tags,
                        String activityName, List<String> zipCodes){
        this.creator = creator;
        this.description = description;
        this.dateAndTime = dateAndTime;
        this.uuid = uuid;
        this.tags = tags;
        this.activityName = activityName;
        this.zipCodes = zipCodes;
    }

    public User getCreator(){
        return this.creator;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setTime(Date dateAndTime){
        this.dateAndTime = dateAndTime;
    }

    public Date getTime(){
        return this.dateAndTime;
    }

    public String getUuid(){
        return this.uuid;
    }

    public void setTags(List<String> tags){
        this.tags = tags;
    }

    public List<String> getTags(){
        return this.tags;
    }

    public void setActivityName(String activityName){ this.activityName = activityName; }

    public String getActivityName(){ return this.activityName; }

    public void setZipCodes(List<String> zipCodes){ this.zipCodes = zipCodes; }

    public List<String> getZipCodes(){ return this.zipCodes; }
}
