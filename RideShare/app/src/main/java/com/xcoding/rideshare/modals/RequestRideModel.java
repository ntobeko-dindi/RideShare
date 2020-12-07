package com.xcoding.rideshare.modals;

public class RequestRideModel {
    private String rideSourceID,rideSourceName,beginning, end, date,time;

    public String getRideSourceID() {
        return rideSourceID;
    }

    public void setRideSourceID(String rideSourceID) {
        this.rideSourceID = rideSourceID;
    }

    public String getRideSourceName() {
        return rideSourceName;
    }

    public void setRideSourceName(String rideSourceName) {
        this.rideSourceName = rideSourceName;
    }

    public String getBeginning() {
        return beginning;
    }

    public void setBeginning(String beginning) {
        this.beginning = beginning;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
