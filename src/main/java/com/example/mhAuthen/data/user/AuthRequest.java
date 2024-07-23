package com.example.demo_mhdigital.data.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AuthRequest {
    @NotNull
    @Length(min = 5, max = 50)
    private String phone;

    @JsonProperty("plainPassword")
    @NotNull @Length(min = 5, max = 50)
    private String password;
}
