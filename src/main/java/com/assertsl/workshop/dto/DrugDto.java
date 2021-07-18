package com.assertsl.workshop.dto;

import java.math.BigDecimal;

public class DrugDto {

    private String productNdc;
    private BigDecimal price;
    private Integer existences;
    private String status = "ACTIVE";

    public String getProductNdc() {
        return productNdc;
    }

    public void setProductNdc(String productNdc) {
        this.productNdc = productNdc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getExistences() {
        return existences;
    }

    public void setExistences(Integer existences) {
        this.existences = existences;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
