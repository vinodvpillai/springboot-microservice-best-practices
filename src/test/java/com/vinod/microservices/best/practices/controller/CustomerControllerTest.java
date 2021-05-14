package com.vinod.microservices.best.practices.controller;

import com.vinod.microservices.best.practices.dto.CustomerDto;
import com.vinod.microservices.best.practices.dto.CustomerRegisterDto;
import com.vinod.microservices.best.practices.dto.CustomerUpdateDto;
import com.vinod.microservices.best.practices.service.ICustomerService;
import com.vinod.microservices.best.practices.util.GlobalUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.vinod.microservices.best.practices.util.ApplicationConstant.CUSTOMER_SERVICE;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @MockBean
    private ICustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void customerAddedWhenMandatoryFieldsAreNotMissing() throws Exception {
        CustomerRegisterDto customerRegisterDto = createCustomerRegisterDto("Ashok","ashok@yopmail.com","Gujarat");
        doNothing().when(customerService).addCustomer(customerRegisterDto);

        mockMvc.perform(post(CUSTOMER_SERVICE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GlobalUtility.convertObjectToJson(customerRegisterDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully added the customer details."));
    }

    @Test
    void addCustomerThrowMethodArgumentNotValidExceptionWhenMandatoryFieldsAreMissing() throws Exception {
        CustomerRegisterDto customerRegisterDto = createCustomerRegisterDto(null,"ashok@yopmail.com","Gujarat");

        mockMvc.perform(post(CUSTOMER_SERVICE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GlobalUtility.convertObjectToJson(customerRegisterDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1002));
    }

    @Test
    void customerAccountUpdatedWhenMandatoryFieldsAreNotMissing() throws Exception {
        final String EMAIL_ID = "ashok@yopmail.com";

        CustomerUpdateDto customerUpdateDto = createCustomerUpdateDto("Ashok","Gujarat");
        doNothing().when(customerService).updateCustomer(customerUpdateDto,EMAIL_ID);

        mockMvc.perform(put("/v1/customers/{emailId}",EMAIL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GlobalUtility.convertObjectToJson(customerUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully updated the customer details."));
    }

    @Test
    void updateCustomerAccountThrowMethodArgumentNotValidExceptionWhenMandatoryFieldsAreMissing() throws Exception {
        final String EMAIL_ID = "ashok@yopmail.com";
        CustomerUpdateDto customerUpdateDto = createCustomerUpdateDto(null,"Gujarat");

        mockMvc.perform(put("/v1/customers/{emailId}",EMAIL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GlobalUtility.convertObjectToJson(customerUpdateDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1002));
    }

    @Test
    void updateCustomerAccountThrowConstraintViolationExceptionWhenPathVariableIsInvalid() throws Exception {
        final String EMAIL_ID = "ashok";
        CustomerUpdateDto customerUpdateDto = createCustomerUpdateDto(null,"Gujarat");

        mockMvc.perform(put("/v1/customers/{emailId}",EMAIL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GlobalUtility.convertObjectToJson(customerUpdateDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1002));
    }

    @Test
    void customerAccountDeletedWhenMandatoryFieldsAreNotMissing() throws Exception {
        final String EMAIL_ID = "ashok@yopmail.com";

        doNothing().when(customerService).deleteCustomer(EMAIL_ID);

        mockMvc.perform(delete("/v1/customers/{emailId}",EMAIL_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully deleted the customer."));
    }

    @Test
    void deleteCustomerAccountThrowConstraintViolationExceptionWhenPathVariableIsInvalid() throws Exception {
        final String EMAIL_ID = "ashok";

        doNothing().when(customerService).deleteCustomer(EMAIL_ID);

        mockMvc.perform(delete("/v1/customers/{emailId}",EMAIL_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(1002));
    }

    @Test
    void getCustomerAccount() throws Exception {
        final String EMAIL_ID = "ashok@yopmail.com";
        CustomerDto customerDto = createCustomerDto(1L,"Ashok","ashok@yopmail.com","Gujarat");
        when(customerService.getCustomerByEmailId(EMAIL_ID)).thenReturn(customerDto);

        mockMvc.perform(get("/v1/customers/{emailId}",EMAIL_ID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully fetched customer."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(customerDto.getId()));
    }

    private CustomerRegisterDto createCustomerRegisterDto(String name, String emailId, String address) {
        return CustomerRegisterDto.builder()
                .name(name)
                .emailId(emailId)
                .address(address)
                .build();
    }

    private CustomerUpdateDto createCustomerUpdateDto(String name, String address) {
        return CustomerUpdateDto.builder()
                .name(name)
                .address(address)
                .build();
    }

    private CustomerDto createCustomerDto(Long id,String name, String emailId, String address) {
        return CustomerDto.builder()
                .id(id)
                .name(name)
                .emailId(emailId)
                .address(address)
                .build();
    }

}