package com.example.appbookinghotel;

public class Order {
    String id;
    String emailuser;
    String soluongphongvakhach;
    String thoigiannhan;
    String thoigiantra;
    String roomname;
    String hotelname;
    String price;
    String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public String getSoluongphongvakhach() {
        return soluongphongvakhach;
    }

    public void setSoluongphongvakhach(String soluongphongvakhach) {
        this.soluongphongvakhach = soluongphongvakhach;
    }

    public String getThoigiannhan() {
        return thoigiannhan;
    }

    public void setThoigiannhan(String thoigiannhan) {
        this.thoigiannhan = thoigiannhan;
    }

    public String getThoigiantra() {
        return thoigiantra;
    }

    public void setThoigiantra(String thoigiantra) {
        this.thoigiantra = thoigiantra;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Order(String id, String emailuser, String soluongphongvakhach, String thoigiannhan, String thoigiantra, String roomname, String hotelname, String price, String location) {
        this.id = id;
        this.emailuser = emailuser;
        this.soluongphongvakhach = soluongphongvakhach;
        this.thoigiannhan = thoigiannhan;
        this.thoigiantra = thoigiantra;
        this.roomname = roomname;
        this.hotelname = hotelname;
        this.price = price;
        this.location = location;
    }
}
