package com.development.unobtainium.cimedic2.responses;

import com.development.unobtainium.cimedic2.models.Doctor;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by unobtainium on 9/10/16.
 */
public class DoctorsResponse {
    public ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    public ServiceError error;
}
