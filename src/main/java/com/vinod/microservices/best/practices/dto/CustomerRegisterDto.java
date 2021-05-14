package com.vinod.microservices.best.practices.dto;

import com.vinod.microservices.best.practices.validation.AddressValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisterDto implements Serializable {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name length should be between 10 and 50")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please email enter a valid email address")
    private String emailId;

    @AddressValidation
    private String address;
}
