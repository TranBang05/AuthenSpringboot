package com.example.demo_mhdigital.controllers;

import com.example.demo_mhdigital.data.request.ChangePasswordRequest;
import com.example.demo_mhdigital.data.request.RoleRequest;
import com.example.demo_mhdigital.data.request.UserRequest;
import com.example.demo_mhdigital.data.response.MessageResponses;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import com.example.demo_mhdigital.service.UserService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/detail/user/{id}")
    public Single<ResponseEntity<UserResponse>> detail(
            @PathVariable("id") Integer id,
            Authentication authentication) {
        return userService.detail(id, authentication)
                .map((ResponseEntity::ok));
    }

    @PostMapping("/add/user")
    public Single<ResponseEntity<UserResponse>> insert(
            @RequestBody UserRequest request,
            Authentication authentication) {
        return userService.insert(request,authentication)
                .map((ResponseEntity::ok));
    }

    @PutMapping("/update/user/{id}")
    public Single<ResponseEntity<UserResponse>> update(
            @PathVariable("id") Integer id,
            @RequestBody UserRequest request,
            Authentication authentication) {
        return userService.update(id, request, authentication)
                .map((ResponseEntity::ok));
    }

    @DeleteMapping("/delete/user/{id}")
    public Single<ResponseEntity<MessageResponses>> delete(
            @PathVariable("id") Integer id,
            Authentication authentication) {
        return userService.delete(id,authentication)
                .map((ResponseEntity::ok));
    }

    @PostMapping("/user/change-password")
    public Single<ResponseEntity<MessageResponses>> changePassword (
            @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request)
                .map((ResponseEntity::ok));
    }

    @PutMapping("/update/{id}")
    public Single<ResponseEntity<String>> updateRole(@PathVariable("id") Integer id , @RequestBody UserRequest userRequest,Authentication authentication) {

        return userService.updateUserRole(id,userRequest,authentication).map((ResponseEntity::ok));
    }

}
