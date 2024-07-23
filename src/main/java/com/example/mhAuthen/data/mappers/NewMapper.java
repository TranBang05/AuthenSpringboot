package com.example.demo_mhdigital.data.mappers;

import com.example.demo_mhdigital.data.request.NewRequest;
import com.example.demo_mhdigital.data.response.NewResponse;
import org.com.app.tables.pojos.New;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewMapper {

    New toNewPojo(NewRequest newRequest);


    NewResponse toRes(New pojo);

    List<NewResponse> toResp(List<New> pojos);



}
