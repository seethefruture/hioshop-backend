package com.example.po;

import lombok.Data;

@Data
public class OrderDto {
    private int addressId;
    private double freightPrice;
    private boolean offlinePay;
    private String postscript;

    // getters and setters
}
