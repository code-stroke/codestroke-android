package com.simi.codestrokealert.model;

public class CaseCompleteManagements {
    private int case_id;
    private String status;
    private String completed_timestamp;


    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getcompleted_timestamp() {
        return completed_timestamp;
    }

    public void setcompleted_timestamp(String completed_timestamp) {
        this.completed_timestamp = completed_timestamp;
    }

}

