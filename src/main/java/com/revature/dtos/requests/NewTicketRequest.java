package com.revature.dtos.requests;

import java.math.BigDecimal;

public class NewTicketRequest {
    private BigDecimal amount;

    private String description;

    private String type_id;

    private String status_id;

    NewTicketRequest() {
        super();
    }

    public NewTicketRequest(BigDecimal amount, String description, String type_id, String status_id) {
        this.amount = amount;
        this.description = description;
        this.type_id = type_id;
        this.status_id = status_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    @Override
    public String toString() {
        return "NewTicketRequest{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", type_id='" + type_id + '\'' +
                ", status_id='" + status_id + '\'' +
                '}';
    }
}
