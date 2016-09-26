package com.development.unobtainium.cimedic2.responses;


import com.development.unobtainium.cimedic2.models.Patient;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by Kevin on 9/21/16.
 */
public class RelativesResponse {
    public ArrayList<Patient> relatives = new ArrayList<Patient>();
    public ServiceError error;
}
