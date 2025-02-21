package com.example.chatting30.Fragment;

import com.example.chatting30.notification.MyResponse;
import com.example.chatting30.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA3qwLPSE:APA91bExlzajJYPaysCoqKjUFuXW83Ms9v5dMOeXs-VXtF32PJgmehM_7JUmTJUMBjTJl6FNKfurKEDB1QDZ4WvDcnL8vBCPVqB8olcvPuN5b4pgF-Und5ZnQIvf8VbrBsbLIfN5leGo"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}