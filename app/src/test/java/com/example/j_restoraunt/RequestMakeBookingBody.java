package com.example.j_restoraunt;

public class RequestMakeBookingBody {
    String restaraunt_label;
    String table_size;
    String time_booking;

    public RequestMakeBookingBody(String restaraunt_label, String table_size, String time_booking) {
        this.restaraunt_label = restaraunt_label;
        this.table_size = table_size;
        this.time_booking = time_booking;
    }
}
