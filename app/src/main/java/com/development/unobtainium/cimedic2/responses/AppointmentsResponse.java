package com.development.unobtainium.cimedic2.responses;

import com.development.unobtainium.cimedic2.models.Appointment;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by unobtainium on 16/10/16.
 */
public class AppointmentsResponse {
    public ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    public ServiceError error;
}
