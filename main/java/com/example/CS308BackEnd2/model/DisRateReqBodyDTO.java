package com.example.CS308BackEnd2.model;

import java.util.List;

public class DisRateReqBodyDTO {
    private List<Double> discountRateList;
    private List<Long> prodIDList;

    public List<Double> getDiscountRateList() {
        return discountRateList;
    }

    public void setDiscountRateList(List<Double> discountRateList) {
        this.discountRateList = discountRateList;
    }

    public List<Long> getProdIDList() {
        return prodIDList;
    }

    public void setProdIDList(List<Long> prodIDList) {
        this.prodIDList = prodIDList;
    }
}
