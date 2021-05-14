package com.vinod.microservices.best.practices.controller;

import com.vinod.microservices.best.practices.dto.CustomerDto;
import com.vinod.microservices.best.practices.dto.CustomerRegisterDto;
import com.vinod.microservices.best.practices.dto.CustomerUpdateDto;
import com.vinod.microservices.best.practices.exception.UserNotFoundException;
import com.vinod.microservices.best.practices.service.ICustomerService;
import com.vinod.microservices.best.practices.util.ResourceMessage;
import com.vinod.microservices.best.practices.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static com.vinod.microservices.best.practices.util.ApplicationConstant.CUSTOMER_SERVICE;
import static com.vinod.microservices.best.practices.util.GlobalUtility.buildResponseForSuccess;

@RestController
@RequestMapping(CUSTOMER_SERVICE)
@Log4j2
@Validated
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ResourceMessage resourceMessage;

    /**
     * This endpoint is for adding new customer information into the system.
     *
     * @param customerRegisterDto   - CustomerRegisterDto object.
     * @return                      - Response.
     */
    @Operation(summary = "This endpoint is for adding new customer information into the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added the customer details to the system.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Exception occurred while adding the customer details to the system.",content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping
    @Trace
    public ResponseEntity<Response> addNewCustomer(@Valid @RequestBody CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to add new customer with following details: {}", customerRegisterDto);
        customerService.addCustomer(customerRegisterDto);
        return buildResponseForSuccess(HttpStatus.SC_OK,resourceMessage.getMessage("customer.added.successfully"),null);
    }

    /**
     * This endpoint is for updating the existing customer details into the system.
     *
     * @param emailId               - Customer Email Id.
     * @param customerUpdateDto     - CustomerUpdateDto object.
     * @return                      - Response.
     */
    @Operation(summary = "This endpoint is for updating the existing customer details into the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the customer details.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Given customer information not found in the system.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Exception occurred while updating the customer details into the system.",content = {@Content(mediaType = "application/json")}),
    })
    @PutMapping("/{emailId}")
    @Trace
    public ResponseEntity<Response> updateCustomer(@PathVariable("emailId") @Email(message = "Please email enter a valid email address.") String emailId, @Valid  @RequestBody CustomerUpdateDto customerUpdateDto) throws UserNotFoundException {
        log.trace("Request came to update customer details: {}", customerUpdateDto);
        customerService.updateCustomer(customerUpdateDto, emailId);
        return buildResponseForSuccess(HttpStatus.SC_OK,resourceMessage.getMessage("customer.updated.successfully"),null);
    }

    /**
     * This endpoint is for deleting the existing customer details from the system.
     *
     * @param emailId   -   Customer Email Id.
     * @return          -   String msg.
     */
    @Operation(summary = "This endpoint is for deleting the existing customer details from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the customer details.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Given customer information not found in the system.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Exception occurred while deleting the customer details from the system.",content = {@Content(mediaType = "application/json")}),
    })
    @DeleteMapping("/{emailId}")
    @Trace
    public ResponseEntity<Response> deleteCustomer(@PathVariable("emailId") @Email(message = "Please email enter a valid email address.") String emailId) throws UserNotFoundException {
        log.trace("Request came to get the customer details having the email id: {}", emailId);
        customerService.deleteCustomer(emailId);
        return buildResponseForSuccess(HttpStatus.SC_OK,resourceMessage.getMessage("customer.deleted.successfully"),null);
    }

    /**
     * Get customer details base on the customer Email id.
     *
     * @param emailId   - Customer Email Id.
     * @return          - CustomerDto object.
     * @throws UserNotFoundException
     */
    @Operation(summary = "This endpoint to get the customer details information from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched customer details.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Given customer information not found in the system.",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Exception occurred while fetching the customer details from the system.",content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/{emailId}")
    @Trace
    public ResponseEntity<Response> getCustomer(@PathVariable("emailId") @Email(message = "Please email enter a valid email address.") String emailId) throws UserNotFoundException {
        log.info("Request came to get the customer details having the email id: {}", emailId);
        CustomerDto customerDto= customerService.getCustomerByEmailId(emailId);
        return buildResponseForSuccess(HttpStatus.SC_OK,resourceMessage.getMessage("customer.fetched.successfully"),customerDto);
    }
}
