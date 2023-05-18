package com.example.appbookinghotel;

public class YeuThich {
    String ID;
    String RoomID;

    public YeuThich(String ID, String roomID) {
        this.ID = ID;
        RoomID = roomID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }
}
