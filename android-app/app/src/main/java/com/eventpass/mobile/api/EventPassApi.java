package com.eventpass.mobile.api;

import com.eventpass.mobile.model.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface EventPassApi {
    @POST("auth/login") Call<User> login(@Body LoginRequest request);
    @POST("auth/register") Call<User> register(@Body RegisterRequest request);
    @GET("events") Call<List<Event>> events();
    @GET("events/{id}") Call<Event> event(@Path("id") Long id);
    @POST("events") Call<Event> createEvent(@Body Event event);
    @PUT("events/{id}") Call<Event> updateEvent(@Path("id") Long id, @Body Event event);
    @DELETE("events/{id}") Call<Void> deleteEvent(@Path("id") Long id);
    @POST("registrations") Call<Registration> subscribe(@Query("usuarioId") Long usuarioId, @Query("eventoId") Long eventoId);
    @GET("registrations/user/{usuarioId}") Call<List<Registration>> registrations(@Path("usuarioId") Long usuarioId);
    @POST("access/scan") Call<ScanResponse> scan(@Body ScanRequest request);
    @GET("access/dashboard") Call<DashboardStats> dashboard();
    @GET("access/history") Call<List<AccessLog>> history();
}
