package com.example.demo_mhdigital.controllers;

import com.example.demo_mhdigital.data.request.RoleRequest;
import com.example.demo_mhdigital.data.response.MessageResponses;
import com.example.demo_mhdigital.data.response.user.RoleResponse;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import com.example.demo_mhdigital.service.impl.RedisConnectionService;
import com.example.demo_mhdigital.service.RoleService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    RoleService roleService;


    @Autowired
    RedisConnectionService redisConnectionService;

    @GetMapping("/detail/role/{id}")
    public Single<ResponseEntity<RoleResponse>> detail(
            @PathVariable("id") Integer id,
            Authentication authentication) {
        return roleService.detail(id, authentication)
                .map((ResponseEntity::ok));
    }

    @PostMapping("/insert/role")
    public Single<ResponseEntity<RoleResponse>> insert(
            RoleRequest roleRequest,
            Authentication authentication){
        return roleService.insert(roleRequest,authentication)
                .map((ResponseEntity::ok));
    }

    @PutMapping("/update/role/{id}")
    public Single<ResponseEntity<RoleResponse>> update(
            @PathVariable Integer id,
            RoleRequest roleRequest,
            Authentication authentication){
        return roleService.update(id,roleRequest,authentication)
                .map((ResponseEntity::ok));
    }

    @DeleteMapping("/delete/role/{id}")
    public Single<ResponseEntity<MessageResponses>> delete(
            @PathVariable Integer id,
            Authentication authentication){
        return roleService.delete(id,authentication)
                .map((ResponseEntity::ok));
    }


    // update quyền của vai trò
    @PutMapping("/update/{id}")
    public Single<ResponseEntity<String>> updatePermissionByRole(@PathVariable("id") Integer id ,
                                                     @RequestBody RoleRequest roleRequest,
                                                     Authentication authentication ) {
        return roleService.updatePermissionByRole(id,roleRequest,authentication).map((ResponseEntity::ok));
    }



    // su dung redis de test toc do
    @GetMapping("/role/{id}")
    public  List<String> getRole(@PathVariable("id") Integer id) {
        return roleService.getRole(id)
                ;
    }

    // kiem tra xem có kết nói được với redis hay không
    @GetMapping("/check-redis-connection")
    public String checkRedisConnection() {
        // Gọi phương thức checkRedisConnection khi có yêu cầu đến API
        redisConnectionService.checkRedisConnection();
        return "Checked Redis Connection";
    }



}
