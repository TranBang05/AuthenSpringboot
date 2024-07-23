package com.example.demo_mhdigital.data.response;

import com.example.demo_mhdigital.data.response.user.RoleResponse;
import com.example.demo_mhdigital.data.response.user.UserResponse;

import java.util.List;

public class UserWithRolesAndPermissions {
    private UserResponse user;
    private List<RoleResponse> roles;

    public UserWithRolesAndPermissions(UserResponse user, List<RoleResponse> roles) {
        this.user = user;
        this.roles = roles;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponse> roles) {
        this.roles = roles;
    }
}
