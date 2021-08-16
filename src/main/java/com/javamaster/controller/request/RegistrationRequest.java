package com.javamaster.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;
}
