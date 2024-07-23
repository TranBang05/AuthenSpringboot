package com.example.demo_mhdigital.data.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PermissionResponse {
    private Integer id;
    private String actionName;
    private String actionCode;
    private String description;
}
