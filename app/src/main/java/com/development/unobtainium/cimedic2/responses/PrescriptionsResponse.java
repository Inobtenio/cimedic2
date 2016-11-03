package com.development.unobtainium.cimedic2.responses;

import com.development.unobtainium.cimedic2.models.Prescription;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by kevin on 02/11/16.
 */

public class PrescriptionsResponse {
    public ArrayList<Prescription> prescriptions = new ArrayList<Prescription>();
    public ServiceError error;
}
