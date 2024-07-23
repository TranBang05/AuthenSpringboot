package com.example.demo_mhdigital.data.mappers;

import com.example.demo_mhdigital.data.request.PermissionRequest;
import com.example.demo_mhdigital.data.response.user.PermissionResponse;
import org.com.app.tables.pojos.Permision;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permision toPojo(PermissionRequest permissionRequest);

    PermissionResponse toResponse(Permision pojo);

    List<PermissionResponse> toResponses(List<Permision> pojos);
}
