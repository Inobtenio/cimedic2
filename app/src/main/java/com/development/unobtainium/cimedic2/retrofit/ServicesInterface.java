package com.development.unobtainium.cimedic2.retrofit;


import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.responses.ClinicsResponse;
import com.development.unobtainium.cimedic2.responses.PatientResponse;
import com.development.unobtainium.cimedic2.responses.RelativesResponse;
import okhttp3.RequestBody;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Kevin on 9/16/16.
 */
public interface ServicesInterface {
    @GET("log_in")
    Call<PatientResponse> getPatient(@Query("email") String email, @Query("password") String password);

    @Multipart
    @POST("./")
    Call<PatientResponse> registerPatient(@Header("Authorization") String authorization, @Part MultipartBody.Part picture, @Part("patient[names]") okhttp3.RequestBody names);

    @GET("relatives")
    Call<RelativesResponse> getRelatives(@Header("Authorization") String authorization);

    @GET("clinics")
    Call<ClinicsResponse> getClinics(@Header("Authorization") String authorization);
}
