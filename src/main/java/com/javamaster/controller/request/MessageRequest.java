package com.javamaster.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MessageRequest {
    @NotEmpty
    String message;

    String hashTags;
}
