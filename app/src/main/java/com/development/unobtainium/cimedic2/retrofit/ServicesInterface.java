package com.development.unobtainium.cimedic2.retrofit;


import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.responses.ClinicsResponse;
import com.development.unobtainium.cimedic2.responses.PatientResponse;
import com.development.unobtainium.cimedic2.responses.RelativesResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Kevin on 9/16/16.
 */
public interface ServicesInterface {
    @GET("log_in")
    Call<PatientResponse> getPatient(@Query("email") String email, @Query("password") String password);

    @POST("./")
    Call<PatientResponse> registerPatient(@Header("Authorization") String authorization, @Body Patient patient);

    @GET("relatives")
    Call<RelativesResponse> getRelatives(@Header("Authorization") String authorization);

    @GET("clinics")
    Call<ClinicsResponse> getClinics(@Header("Authorization") String authorization);
}
