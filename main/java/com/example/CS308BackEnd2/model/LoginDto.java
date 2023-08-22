package com.example.CS308BackEnd2.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginDto {
    private String email;
    private String password;
    private List<CartItemDTO> items;
}
