package com.xcoding.rideshare.modals;

public class PendingRides {
    private String driverID,passengerID,bookedSits,expiryDate,totalRidePrice;

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passenderID) {
        this.passengerID = passenderID;
    }

    public String getBookedSits() {
        return bookedSits;
    }

    public void setBookedSits(String bookedSits) {
        this.bookedSits = bookedSits;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTotalRidePrice() {
        return totalRidePrice;
    }

    public void setTotalRidePrice(String totalRidePrice) {
        this.totalRidePrice = totalRidePrice;
    }
}
