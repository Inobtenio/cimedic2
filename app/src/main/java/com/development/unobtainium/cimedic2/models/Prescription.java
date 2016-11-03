package com.development.unobtainium.cimedic2.models;

/**
 * Created by kevin on 02/11/16.
 */

public class Prescription {
    private String reason;
    private String diagnosis;
    private Integer expiration_date;
    private Clinic clinic;

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Integer getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Integer expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Clinic getClinic() {
        return clinic;
    }


}
