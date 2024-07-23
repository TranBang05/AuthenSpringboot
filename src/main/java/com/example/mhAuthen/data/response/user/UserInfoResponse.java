package com.example.demo_mhdigital.data.response.user;

import lombok.Data;
import org.com.app.tables.pojos.User;


@Data
public class UserInfoResponse {
    private User user;

    private String token;
}
