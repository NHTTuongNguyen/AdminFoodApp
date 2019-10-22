package com.example.admin_project.Model;

public class Rating {
    private String userPhone; /// both key and value
    private String carsId;
    private String name;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String carsId, String name, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.carsId = carsId;
        this.name = name;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCarsId() {
        return carsId;
    }

    public void setCarsId(String carsId) {
        this.carsId = carsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
