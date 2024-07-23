package com.example.demo_mhdigital.data.mappers;

import com.example.demo_mhdigital.data.request.RoleRequest;
import com.example.demo_mhdigital.data.response.user.RoleResponse;
import org.com.app.tables.pojos.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toPojo(RoleRequest roleRequest);


    RoleResponse toResponse(Role pojo);

    List<RoleResponse> toResponses(List<Role> pojos);


}
