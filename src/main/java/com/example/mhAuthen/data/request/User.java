package com.example.demo_mhdigital.data.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private Integer hasPassword;
    private String plainPassword;
    private Timestamp regisDate;

    //Sử dụng trong tình huống đổi mật khẩu
    private String newPassword;
    private Integer gender;
    private String facebookLink;
    private String role;
    private String note;
    private Integer active;
    private String otp;

    //Truờng hợp vừa lấy thông tin đủ, vừa lấy về token (Login bằng fb, google)
    private String token;
}
