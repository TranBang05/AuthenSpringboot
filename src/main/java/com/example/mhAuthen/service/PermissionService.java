package com.example.demo_mhdigital.service;

import com.example.demo_mhdigital.data.request.PermissionRequest;
import io.reactivex.rxjava3.core.Single;

public interface PermissionService {

    Single<String> addPermission( PermissionRequest permissionRequest);

}
