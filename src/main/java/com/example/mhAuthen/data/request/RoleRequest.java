package com.example.demo_mhdigital.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RoleRequest {

    private String roleName;
    private String roleCode;
    private String description;
    private List<Integer> permissionIds;

}
