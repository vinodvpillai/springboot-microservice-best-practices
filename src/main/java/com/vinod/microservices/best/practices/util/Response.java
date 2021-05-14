package com.vinod.microservices.best.practices.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private int status;
    private String message;
    private Object data;
    private String errorCode;
    private String errorMessage;
    private Object errorData;
}
