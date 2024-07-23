package com.example.demo_mhdigital.data.response;

import lombok.Data;

@Data
public class MessageResponses extends BaseResponse {
    private String content;

    public MessageResponses(String content) {
        this.content = content;
    }
}
