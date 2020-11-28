package com.xcoding.rideshare.modals;

public class ShortDistanceCommuteModal {
    private String rideSource, beginning, end, startDate, endDate, sits, price;

    public ShortDistanceCommuteModal() {
    }

    public ShortDistanceCommuteModal(String rideSource,String beginning, String end, String startDate, String endDate, String sits, String price) {
        this.rideSource = rideSource;
        this.beginning = beginning;
        this.end = end;
        this.startDate = startDate;
        this.endDate = endDate;
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
