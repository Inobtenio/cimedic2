package com.development.unobtainium.cimedic2.responses;

import com.development.unobtainium.cimedic2.models.Schedule;
import com.development.unobtainium.cimedic2.retrofit.ServiceError;

import java.util.ArrayList;

/**
 * Created by unobtainium on 11/10/16.
 */
public class SchedulesResponse {
    public ArrayList<Schedule> schedules = new ArrayList<Schedule>();
    public ServiceError error;
}
