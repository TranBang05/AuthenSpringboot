package com.example.demo_mhdigital.service;

import com.example.demo_mhdigital.data.constant.MessageResponse;
import com.example.demo_mhdigital.data.request.ChangePasswordRequest;
import com.example.demo_mhdigital.data.request.RoleRequest;
import com.example.demo_mhdigital.data.request.UserRequest;
import com.example.demo_mhdigital.data.request.UserRoleRequest;
import com.example.demo_mhdigital.data.response.MessageResponses;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import com.example.demo_mhdigital.data.user.AuthRequest;
import com.example.demo_mhdigital.data.user.TokenResponse;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.UserRole;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Single<List<UserResponse>> getUsers();

    Single<String> addUser(UserRequest userRequest);

    Single<String> updateUser(Integer id, UserRequest userRequest);

    Single<String> deleteUser(Integer id);

     Single<Optional<UserResponse>> getById(Integer id);


    Single<TokenResponse> authenticate(AuthRequest request);
    Single<MessageResponses> delete(Integer id, Authentication authentication);
    Single<UserResponse> insert(UserRequest request, Authentication authentication);

    Single<UserResponse> update(Integer id, UserRequest request, Authentication authentication);

    Single<UserResponse> detail(Integer id, Authentication authentication);

    Single<MessageResponses> changePassword(ChangePasswordRequest request);


    //Single<MessageResponses> updateRole(UserRoleRequest request);
    Single<String> updateUserRole(Integer id, UserRequest userRoleRequest,Authentication authentication);

}
