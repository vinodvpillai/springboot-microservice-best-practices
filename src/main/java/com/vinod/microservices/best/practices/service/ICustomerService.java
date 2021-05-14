package com.vinod.microservices.best.practices.service;


import com.vinod.microservices.best.practices.dto.CustomerDto;
import com.vinod.microservices.best.practices.dto.CustomerRegisterDto;
import com.vinod.microservices.best.practices.dto.CustomerUpdateDto;
import com.vinod.microservices.best.practices.exception.UserNotFoundException;

public interface ICustomerService {

    /**
     * Get customer object by customer email id.
     *
     * @param emailId    - Customer Email ID.
     * @return      - Customer query object.
     */
    CustomerDto getCustomerByEmailId(final String emailId) throws UserNotFoundException;

    /**
     * Add customer object to database.
     *
     * @param customerRegisterDto - Customer register object.
     */
    void addCustomer(final CustomerRegisterDto customerRegisterDto);

    /**
     * Add customer object to database.
     *  @param customerUpdateDto - Customer update object.
     * @param emailId
     */
    void updateCustomer(final CustomerUpdateDto customerUpdateDto, final String emailId) throws UserNotFoundException;

    /**
     * Delete customer by customer email id.
     *
     * @param emailId - Customer Email id.
     */
    void deleteCustomer(final String emailId) throws UserNotFoundException;
}
