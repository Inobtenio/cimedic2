package com.development.unobtainium.cimedic2.responses;


import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

/**
 * Created by Kevin on 9/16/16.
 */
public class PatientResponse {
    public Patient patient;
    public ServiceError error;
}
