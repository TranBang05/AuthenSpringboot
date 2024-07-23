package com.example.demo_mhdigital.data.request;


import lombok.Data;


@Data

public class ChangePasswordRequest {
    private Integer id;
    private String oldPassword;
    private String password;
}
