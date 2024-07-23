package com.example.demo_mhdigital.config.utils;

import com.example.demo_mhdigital.data.user.UserAuthorDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public class SecurityUtils {
    public static UserAuthorDTO extractUser(Authentication authentication) {
        UserAuthorDTO userAuthorDTO = (UserAuthorDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userAuthorDTO;
    }
}
