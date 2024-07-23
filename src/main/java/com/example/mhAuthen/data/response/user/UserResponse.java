package com.example.demo_mhdigital.data.response.user;

import com.example.demo_mhdigital.data.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class UserResponse extends BaseResponse {

    private String name;
    private String phone;
    private String email;
    private Integer id;
    private List<RoleResponse> roleResponses;

}
