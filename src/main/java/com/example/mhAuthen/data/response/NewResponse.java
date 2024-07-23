package com.example.demo_mhdigital.data.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class NewResponse {
    private String title;
    private String content;
    private String description;
    private Integer categoryNewId;
    private Integer id;
    private CategoryNewResponse categoryNewResponse;
}
