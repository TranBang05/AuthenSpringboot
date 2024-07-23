package com.example.demo_mhdigital.data.user;

public enum Roles {
    ADMIN("ADMIN"),
    USER("USER"),
    SCOUT("SCOUT"),
    VISITOR("VISITOR");

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    Roles(String roleName) {
        this.roleName = roleName;
    }
}
