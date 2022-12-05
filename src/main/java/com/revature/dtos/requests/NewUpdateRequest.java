package com.revature.dtos.requests;

public class NewUpdateRequest {
    private String reimb_id;
    private String status_id;

    public NewUpdateRequest() {
        super();
    }

    public NewUpdateRequest(String reimb_id, String status_id) {
        this.reimb_id = reimb_id;
        this.status_id = status_id;
    }

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

}
