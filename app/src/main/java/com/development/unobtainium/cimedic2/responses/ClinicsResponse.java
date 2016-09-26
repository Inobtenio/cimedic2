package com.development.unobtainium.cimedic2.responses;

import com.development.unobtainium.cimedic2.models.Clinic;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by unobtainium on 25/09/16.
 */
public class ClinicsResponse {
    public ArrayList<Clinic> clinics = new ArrayList<Clinic>();
    public ServiceError error;
}
