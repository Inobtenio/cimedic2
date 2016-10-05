package com.development.unobtainium.cimedic2.responses;

import com.development.unobtainium.cimedic2.models.Specialty;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by unobtainium on 2/10/16.
 */
public class SpecialtiesResponse {
    public ArrayList<Specialty> specialties = new ArrayList<Specialty>();
    public ServiceError error;
}
