package com.example.demo_mhdigital.data.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CategoryNewResponse {
    private String title;
    private String content;
    private Integer categoryNewId;
    private List<NewResponse> newResponses;
}
