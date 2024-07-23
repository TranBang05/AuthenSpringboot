package com.example.demo_mhdigital.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PermissionRequest {
    private String actionName;
    private String actionCode;
    private String description;
}
