package com.vinod.microservices.best.practices.service.impl;


import com.vinod.microservices.best.practices.dto.CustomerDto;
import com.vinod.microservices.best.practices.dto.CustomerMessageData;
import com.vinod.microservices.best.practices.dto.CustomerRegisterDto;
import com.vinod.microservices.best.practices.dto.CustomerUpdateDto;
import com.vinod.microservices.best.practices.event.CustomerCreatedEvent;
import com.vinod.microservices.best.practices.exception.UserNotFoundException;
import com.vinod.microservices.best.practices.model.Customer;
import com.vinod.microservices.best.practices.repository.CustomerRepository;
import com.vinod.microservices.best.practices.service.ICustomerService;
import com.vinod.microservices.best.practices.service.IQueueService;
import lombok.extern.log4j.Log4j2;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.vinod.microservices.best.practices.util.ApplicationConstant.CustomerStatus.REGISTERED;

@Service
@Log4j2
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CustomerCreatedEvent customerCreatedEvent;
    @Autowired
    private IQueueService queueService;

    @Value("${queue.customer.deleted}")
    private String queue_customer_deleted;

    /**
     * Add customer object to database and raise event.
     *
     * @param customerRegisterDto - Customer register command object.
     */
    @Override
    @Trace
    public void addCustomer(CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to add new customer : {}", customerRegisterDto);
        Customer customer = mapAndSaveCustomerDetails(customerRegisterDto);
        customerCreatedEvent.on(CustomerMessageData.builder().emailId(customer.getEmailId()).firstName(customer.getName()).build());
        log.info("Successfully saved customer object: {}", customer);
    }

    /**
     * Update the customer object to database.
     *
     * @param customerUpdateDto - Customer update dto object.
     * @param emailId           - Customer Email ID.
     */
    @Override
    @Trace
    public void updateCustomer(CustomerUpdateDto customerUpdateDto, String emailId) throws UserNotFoundException {
        log.trace("Request came to update customer details for: {}", customerUpdateDto);
        Customer customer = mapDataToCustomer(customerUpdateDto, emailId);
        if(null!=customer) {
            Customer persistedCustomer=customerRepository.save(customer);
            log.info("Successfully updated customer details: {}", persistedCustomer);
        } else {
            throw new UserNotFoundException("Customer update operation failed, customer not found for email id:"+emailId);
        }
    }

    /**
     * Delete customer by customer emailId.
     *
     * @param emailId - Customer emailId.
     */
    @Override
    @Trace
    public void deleteCustomer(String emailId) throws UserNotFoundException {
        log.trace("Request came to delete customer having emailId: {}", emailId);
        Optional<Customer> optionalCustomer=customerRepository.findCustomerByEmailId(emailId);
        if(optionalCustomer.isPresent()) {
            Customer  customer = optionalCustomer.get();
            customerRepository.delete(customer);
            queueService.sendMessage(queue_customer_deleted, CustomerMessageData.builder().emailId(customer.getEmailId()).firstName(customer.getName()).build());
            log.info("Successfully deleted the customer details for customer email id: {}", emailId);
        } else {
            throw new UserNotFoundException("Customer delete operation failed, customer not found for email id:"+emailId);
        }
    }

    /**
     * Get customer object by Email ID.
     *
     * @param emailId    - Customer Email ID.
     * @return      - Customer Query object.
     */
    @Override
    @Trace
    public CustomerDto getCustomerByEmailId(String emailId) throws UserNotFoundException {
        log.trace("Request came to get customer details for customer email id: {}", emailId);
        Customer customer = fetchCustomerDetailsByEmailId(emailId);
        if(null!=customer) {
            return mapDataToCustomerDto(customer);
        } else {
            throw new UserNotFoundException("Get Customer details operation failed, customer not found for email id:"+emailId);
        }
    }

    /**
     * Fetch the customer object from DB using customer email id.
     *
     * @param emailId
     * @return
     */
    private Customer fetchCustomerDetailsByEmailId(String emailId) {
        log.trace("Fetch customer details from DB for customer email id: {}", emailId);
        Optional<Customer> optionalCustomer=customerRepository.findCustomerByEmailId(emailId);
        return optionalCustomer.isPresent() ? optionalCustomer.get() : null;
    }

    /**
     * Save the customer object to DB.
     *
     * @param customer
     * @return
     */
    private Customer saveCustomerDetails(Customer customer) {
        log.trace("Request came to save the customer details to DB having customer details: {}", customer);
        Customer persistedCustomer = customerRepository.save(customer);
        log.trace("Persisted customer details: {}", persistedCustomer);
        return persistedCustomer;
    }

    /**
     * Save the customer object to database.
     *
     * @param customerRegisterDto - Customer register dto object.
     * @return  - Customer object.
     */
    @Trace
    private Customer mapAndSaveCustomerDetails(CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to map and save the customer object with customer details: {}", customerRegisterDto);
        Customer customer = mapDataToCustomer(customerRegisterDto);
        return saveCustomerDetails(customer);
    }

    /**
     * Map CustomerRegisterDto to Customer object.
     *
     * @param customerRegisterDto - customerRegisterDto object.
     * @return  - Customer object.
     */
    @Trace
    private Customer mapDataToCustomer(CustomerRegisterDto customerRegisterDto) {
        modelMapper.typeMap(CustomerRegisterDto.class, Customer.class).addMappings(mapper -> mapper.skip(Customer::setId));
        Customer customer = modelMapper.map(customerRegisterDto, Customer.class);
        customer.setStatus(REGISTERED.value());
        return customer;
    }

    /**
     * Map Customer to CustomerDto object.
     *
     * @param customer - Customer object.
     * @return  - Customer Query object.
     */
    private CustomerDto mapDataToCustomerDto(Customer customer) {
        modelMapper.typeMap(Customer.class, CustomerDto.class).addMappings(mapper -> mapper.skip(CustomerDto::setId));
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        customerDto.setId(customer.getId());
        return customerDto;
    }

    /**
     * Map CustomerUpdateDto to Customer object.
     *
     * @param customerUpdateDto - customerUpdateDto object.
     * @param emailId
     * @return  - Customer object.
     */
    private Customer mapDataToCustomer(CustomerUpdateDto customerUpdateDto, String emailId) {
        Customer customer = fetchCustomerDetailsByEmailId(emailId);
        if(null!=customer) {
            customer.setAddress(customerUpdateDto.getAddress());
            customer.setName(customerUpdateDto.getName());
            return customer;
        }
        return null;
    }
}
