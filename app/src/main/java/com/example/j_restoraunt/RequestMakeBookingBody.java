package com.example.j_restoraunt;

public class RequestMakeBookingBody {
    private String restaraunt_label;
    private String table_size;
    private String time_booking;
    private String date_booking;

    public RequestMakeBookingBody(String restaraunt_label, String table_size, String time_booking, String date_booking) {
        this.restaraunt_label = restaraunt_label;
        this.table_size = table_size;
        this.time_booking = time_booking;
        this.date_booking = date_booking;
    }
}
