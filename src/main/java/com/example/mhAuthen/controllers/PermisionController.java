package com.example.demo_mhdigital.controllers;

import com.example.demo_mhdigital.data.request.PermissionRequest;
import com.example.demo_mhdigital.service.PermissionService;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PermisionController {

    @Autowired
    PermissionService permissionService;

    @PostMapping("/addPermission")
    public Single<ResponseEntity<String>> addPermission(@RequestBody PermissionRequest permissionRequest) {
        return permissionService.addPermission(permissionRequest).map((ResponseEntity::ok));
    }


}
