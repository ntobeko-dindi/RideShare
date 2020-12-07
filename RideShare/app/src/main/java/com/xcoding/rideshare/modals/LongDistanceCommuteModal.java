package com.xcoding.rideshare.modals;

public class LongDistanceCommuteModal {
    private String rideSourceID,rideSourceName,beginning, destination, date, sits, price,time;

    public LongDistanceCommuteModal() {
    }

    public LongDistanceCommuteModal(String time,String beginning, String end, String date, String sits, String price) {
        this.beginning = beginning;
        this.destination = end;
        this.date = date;
        this.sits = sits;
        this.price = price;
        this.time =time;
    }

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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String end) {
        this.destination = end;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSits() {
        return sits;
    }

    public void setSits(String sits) {
        this.sits = sits;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
