package com.xcoding.rideshare.modals;

public class Rides {

    private String username;
    private String description;
    private Integer image;
    private String date;

    public Rides(String username, String description, Integer image, String date) {
        this.username = username;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
