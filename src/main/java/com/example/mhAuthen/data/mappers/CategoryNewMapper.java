package com.example.demo_mhdigital.data.mappers;

import com.example.demo_mhdigital.data.request.CategoryNewRequest;
import com.example.demo_mhdigital.data.response.CategoryNewResponse;
import org.com.app.tables.pojos.CategoryNew;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryNewMapper {

    CategoryNew toPojo(CategoryNewRequest categoryNewRequest);
    @Mapping(source = "id", target = "categoryNewId")
    CategoryNewResponse toResponse (CategoryNew pojo);

    List<CategoryNewResponse> toResponses (List<CategoryNew> pojos);


}
