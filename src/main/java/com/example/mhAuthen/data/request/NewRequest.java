package com.example.demo_mhdigital.data.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NewRequest {

    private String title;
    private String content;
    private Integer categoryNewId;
    private String description;
}
