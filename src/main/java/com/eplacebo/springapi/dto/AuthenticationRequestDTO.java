package com.eplacebo.springapi.dto;


import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String username;
    private String password;

}
