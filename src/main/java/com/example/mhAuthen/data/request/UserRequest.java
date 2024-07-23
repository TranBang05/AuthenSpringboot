package com.example.demo_mhdigital.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserRequest {

//    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String role;
    private List<Integer> roleId;
}
