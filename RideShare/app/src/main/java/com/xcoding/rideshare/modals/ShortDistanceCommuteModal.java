package com.xcoding.rideshare.modals;

public class ShortDistanceCommuteModal {
    private String time, beginning,destination, startDate, endDate, sits, price;

    public ShortDistanceCommuteModal() {
    }

    public ShortDistanceCommuteModal(String time,String beginning, String destination, String startDate, String endDate, String sits, String price) {
        this.time = time;
        this.beginning = beginning;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sits = sits;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
