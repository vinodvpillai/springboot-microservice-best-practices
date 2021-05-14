package com.vinod.microservices.best.practices.impl;

import com.vinod.microservices.best.practices.dto.CustomerDto;
import com.vinod.microservices.best.practices.dto.CustomerMessageData;
import com.vinod.microservices.best.practices.dto.CustomerRegisterDto;
import com.vinod.microservices.best.practices.dto.CustomerUpdateDto;
import com.vinod.microservices.best.practices.event.CustomerCreatedEvent;
import com.vinod.microservices.best.practices.exception.UserNotFoundException;
import com.vinod.microservices.best.practices.model.Customer;
import com.vinod.microservices.best.practices.repository.CustomerRepository;
import com.vinod.microservices.best.practices.service.ICustomerService;
import com.vinod.microservices.best.practices.service.impl.AWSSQSQueueService;
import com.vinod.microservices.best.practices.util.ApplicationConstant;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.apache.commons.lang3.builder.CompareToBuilder.reflectionCompare;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceTest {

    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private CustomerCreatedEvent customerCreatedEvent;
    @MockBean
    private AWSSQSQueueService queueService;
    @Autowired
    private ICustomerService customerService;
    @Value("${queue.customer.deleted}")
    private String queue_customer_deleted;

    @Test
    void customerAdded() {
        CustomerRegisterDto customerRegisterDto = createCustomerRegisterDto("Ashok","ashok@yopmail.com","Gujarat");
        Customer customer = createCustomer("Ashok","ashok@yopmail.com","Gujarat", ApplicationConstant.CustomerStatus.REGISTERED.value());
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);
        doNothing().when(customerCreatedEvent).on(Mockito.any(CustomerMessageData.class));

        customerService.addCustomer(customerRegisterDto);

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository,times(1)).save(argumentCaptor.capture());
        verify(customerCreatedEvent,times(1)).on(Mockito.any(CustomerMessageData.class));
        Customer customerArgument = argumentCaptor.getValue();
        MatcherAssert.assertThat(reflectionCompare(customer, customerArgument, new String[]{"id"}), Matchers.is(0));
    }

    @Test
    void customerUpdatedWhenCustomerFound() throws UserNotFoundException {
        final String EMAIL_ID = "ashok@yopmail.com";
        CustomerUpdateDto customerUpdateDto = createCustomerUpdateDto("Ashok","Gujarat");
        Customer customer = createCustomer("Ashok",EMAIL_ID,"Gujarat", ApplicationConstant.CustomerStatus.REGISTERED.value());
        customer.setId(1L);
        Optional<Customer> optionalCustomer=Optional.of(customer);
        when(customerRepository.findCustomerByEmailId(EMAIL_ID)).thenReturn(optionalCustomer);
        when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);

        customerService.updateCustomer(customerUpdateDto,EMAIL_ID);

        verify(customerRepository,times(1)).findCustomerByEmailId(EMAIL_ID);
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository,times(1)).save(argumentCaptor.capture());
        Customer customerArgument = argumentCaptor.getValue();
        MatcherAssert.assertThat(reflectionCompare(customer, customerArgument), Matchers.is(0));
    }

    @Test
    void customerNotUpdatedAsCustomerNotFound() {
        final String EMAIL_ID = "customer_not_found@yopmail.com";
        CustomerUpdateDto customerUpdateDto = createCustomerUpdateDto("Ashok","Gujarat");
        Optional<Customer> optionalCustomer=Optional.ofNullable(null);
        when(customerRepository.findCustomerByEmailId(EMAIL_ID)).thenReturn(optionalCustomer);

        String expectedErrorMsg = "Customer update operation failed, customer not found for email id:"+EMAIL_ID;
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            customerService.updateCustomer(customerUpdateDto,EMAIL_ID);
        });

        verify(customerRepository,times(1)).findCustomerByEmailId(EMAIL_ID);
        verify(customerRepository,never()).save(Mockito.any(Customer.class));
        assertTrue(exception.getMessage().contains(expectedErrorMsg));
    }

    @Test
    void deleteCustomer() throws UserNotFoundException {
        final String EMAIL_ID = "ashok@yopmail.com";

        Customer customer = createCustomer("Ashok",EMAIL_ID,"Gujarat", ApplicationConstant.CustomerStatus.REGISTERED.value());
        customer.setId(1L);
        Optional<Customer> optionalCustomer=Optional.of(customer);
        when(customerRepository.findCustomerByEmailId(EMAIL_ID)).thenReturn(optionalCustomer);
        doNothing().when(customerRepository).delete(Mockito.any(Customer.class));

        doNothing().when(queueService).sendMessage(Mockito.any(String.class), Mockito.any(CustomerMessageData.class));

        customerService.deleteCustomer(EMAIL_ID);

        verify(customerRepository,times(1)).findCustomerByEmailId(EMAIL_ID);
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository,times(1)).delete(argumentCaptor.capture());
        verify(queueService,times(1)).sendMessage(Mockito.any(String.class), Mockito.any(CustomerMessageData.class));
        Customer customerArgument = argumentCaptor.getValue();
        MatcherAssert.assertThat(reflectionCompare(customer, customerArgument), Matchers.is(0));
    }

    @Test
    void foundCustomerAsCustomerEmailIdExists() throws UserNotFoundException {
        final String EMAIL_ID = "ashok@yopmail.com";

        Customer customer = createCustomer("Ashok",EMAIL_ID,"Gujarat", ApplicationConstant.CustomerStatus.REGISTERED.value());
        customer.setId(1L);
        Optional<Customer> optionalCustomer=Optional.of(customer);
        when(customerRepository.findCustomerByEmailId(EMAIL_ID)).thenReturn(optionalCustomer);

        CustomerDto customerDto=customerService.getCustomerByEmailId(EMAIL_ID);

        verify(customerRepository,times(1)).findCustomerByEmailId(EMAIL_ID);

        assertEquals(customer.getEmailId(),customerDto.getEmailId());
        assertEquals(customer.getId(),customerDto.getId());
        assertEquals(customer.getAddress(),customerDto.getAddress());
        assertEquals(customer.getName(),customerDto.getName());
    }

    @Test
    void customerNotFoundAsCustomerEmailIdNotExists() {
        final String EMAIL_ID = "customer_not_found@yopmail.com";

        Optional<Customer> optionalCustomer=Optional.ofNullable(null);
        when(customerRepository.findCustomerByEmailId(EMAIL_ID)).thenReturn(optionalCustomer);

        String expectedErrorMsg = "Get Customer details operation failed, customer not found for email id:"+EMAIL_ID;
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            customerService.getCustomerByEmailId(EMAIL_ID);
        });

        verify(customerRepository,times(1)).findCustomerByEmailId(EMAIL_ID);
        assertTrue(exception.getMessage().contains(expectedErrorMsg));
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

    private Customer createCustomer(String name, String emailId, String address, String status) {
        return Customer.builder()
                .name(name)
                .emailId(emailId)
                .address(address)
                .status(status)
                .build();
    }
}