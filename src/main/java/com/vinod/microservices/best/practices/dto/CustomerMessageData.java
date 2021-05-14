package com.vinod.microservices.best.practices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMessageData implements Serializable {

    private String firstName;
    private String emailId;
}
