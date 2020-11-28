package com.xcoding.rideshare.modals;

public class LongDistanceCommuteModal {
    private String rideSource,beginning, end, date, sits, price;

    public LongDistanceCommuteModal() {
    }

    public LongDistanceCommuteModal(String rideSource,String beginning, String end, String date, String sits, String price) {
        this.beginning = beginning;
        this.end = end;
        this.date = date;
        this.sits = sits;
        this.price = price;
    }

    public String getRideSource() {
        return rideSource;
    }

    public void setRideSource(String rideSource) {
        this.rideSource = rideSource;
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
}
