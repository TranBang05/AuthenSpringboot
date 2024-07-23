package com.example.demo_mhdigital.service.impl;

import com.example.demo_mhdigital.data.request.PermissionRequest;
import com.example.demo_mhdigital.data.mappers.PermissionMapper;
import com.example.demo_mhdigital.repositories.PermissionRespository;
import com.example.demo_mhdigital.service.PermissionService;
import io.reactivex.rxjava3.core.Single;
import org.com.app.tables.pojos.Permision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRespository permissionRespository;

    @Autowired
    PermissionMapper mapper;
    @Override
    public Single<String> addPermission(PermissionRequest permissionRequest) {
        Permision permision = mapper.toPojo(permissionRequest);

        return permissionRespository.addPermission(permision)
                .map(permissioninsert->{
                    if (permissioninsert.isPresent()) {
                        return "success";
                    } else {
                        throw new RuntimeException("error");
                    }
                }).onErrorReturnItem("error");


    }
}
