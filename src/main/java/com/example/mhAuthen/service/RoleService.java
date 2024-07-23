package com.example.demo_mhdigital.service;

import com.example.demo_mhdigital.data.request.RoleRequest;
import com.example.demo_mhdigital.data.request.UserRequest;
import com.example.demo_mhdigital.data.response.MessageResponses;
import com.example.demo_mhdigital.data.response.user.RoleResponse;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Single<String> addRole( RoleRequest roleRequest);

    Single<String> updateRole(Integer id, RoleRequest roleRequest);

    Single<String> updatePermissionByRole(Integer id, RoleRequest roleRequest,Authentication authentication);

    Single<String> deleteRole(Integer id);

    @NonNull Single<Optional<?>> getById(Integer id);

    List<String> getRole(Integer id);

    Single<MessageResponses> delete(Integer id, Authentication authentication);
    Single<RoleResponse> insert(RoleRequest request, Authentication authentication);

    Single<RoleResponse> update(Integer id, RoleRequest request, Authentication authentication);

    Single<RoleResponse> detail(Integer id, Authentication authentication);

}
