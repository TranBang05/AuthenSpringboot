package com.example.demo_mhdigital.controllers;

import com.example.demo_mhdigital.config.exception.ApiException;
import com.example.demo_mhdigital.data.user.AuthRequest;
import com.example.demo_mhdigital.data.user.TokenResponse;
import com.example.demo_mhdigital.service.UserService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController  {
    @Autowired
    UserService userService;
    @PostMapping("/user/authenticate")
    public Single<TokenResponse> login(@RequestBody @Valid AuthRequest request) throws ApiException {
        return userService.authenticate(request);
    }
}
