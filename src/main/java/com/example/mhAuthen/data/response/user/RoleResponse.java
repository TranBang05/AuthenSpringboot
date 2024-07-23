package com.example.demo_mhdigital.data.response.user;

import com.example.demo_mhdigital.data.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoleResponse extends BaseResponse {

    private Integer roleId;
    private String roleName;
    private String roleCode;
    private String description;
    private Integer id;
    private List<PermissionResponse> permisionResponses;
}
