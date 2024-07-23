package com.example.demo_mhdigital.data.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserRoleRequest {
    private Integer id;
    private List<Integer> roleId;
}
