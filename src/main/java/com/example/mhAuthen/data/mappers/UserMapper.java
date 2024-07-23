package com.example.demo_mhdigital.data.mappers;

import com.example.demo_mhdigital.data.request.UserRequest;
import com.example.demo_mhdigital.data.response.user.UserResponse;
import org.com.app.tables.pojos.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toPojo(UserRequest userRequest);



    UserResponse toResponse(User pojo);

    List<UserResponse> toResponses(List<User> pojos);

}
