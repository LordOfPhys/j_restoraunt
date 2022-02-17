package com.example.j_restoraunt;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface GerritAPI {
    @POST("/get_restaraunts")
    Call<RequestGetRestaraunts> getRestaraunt(@Body RequestEmpty message);

    @POST("/get_restaraunt_info")
    Call<RequestGetRestarauntsInfo> getRestarauntInfo(@Body RequestGetRestarauntsInfoBody message);

    @POST("/make_booking")
    Call<RequestMakeBooking> makeBooking(@Body RequestMakeBookingBody message);

    @POST("/delete_booking")
    Call<RequestCancelBooking> cancelBooking(@Body RequestCancelBookingBody message);
}