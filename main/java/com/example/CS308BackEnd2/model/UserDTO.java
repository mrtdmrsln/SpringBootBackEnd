package com.example.CS308BackEnd2.model;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    String name;
    int age;
    String email;
    String password;
    User.role role;
    String address;

    String phone;

    User.gender gender;

    List<CartItemDTO> items;


}
