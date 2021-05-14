package com.vinod.microservices.best.practices.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customer")
public class Customer implements Serializable {

    @Schema(description = "Unique identifier of the Customer.", example = "1", required = true)
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Name of the customer.", example = "Vinod Pillai", required = true)
    @NotBlank
    @Size(max = 50)
    @Column
    private String name;

    @Schema(description = "Email address of the customer.", example = "vinod@yopmail.com", required = true)
    @Column
    @Size(max = 150)
    private String emailId;

    @Schema(description = "Address line 1 of the customer.", example = "Gandhinagar, #54", required = false)
    @Column
    @Size(max = 250)
    private String address;

    @Schema(description = "Address line 1 of the contact.", example = "888 Constantine Ave, #54", required = false)
    @Size(max = 50)
    @Column
    private String status;
}
