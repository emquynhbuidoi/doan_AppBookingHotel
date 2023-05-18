package com.example.appbookinghotel;

import android.net.Uri;

import java.io.Serializable;

public class Room implements Serializable {
    private String id;
    private String roomename;
    private String price;
    private String rate;
    private String ImageUri = "0";
    private String hotelID;
    private String location;

    public Room(String id, String roomename, String price, String rate, String imageUri, String hotelID, String location) {
        this.id = id;
        this.roomename = roomename;
        this.price = price;
        this.rate = rate;
        ImageUri = imageUri;
        this.hotelID = hotelID;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomename() {
        return roomename;
    }

    public void setRoomename(String roomename) {
        this.roomename = roomename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getHotelID() {
        return hotelID;
    }

    public void setHotelID(String hotelID) {
        this.hotelID = hotelID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
