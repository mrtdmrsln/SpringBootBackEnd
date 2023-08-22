package com.example.CS308BackEnd2.model;

import lombok.Data;

@Data
public class ChangePriceDTO {
    private Long prodID;
    private Double newPrice;
}
