package com.vinod.microservices.best.practices.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinod.microservices.best.practices.dto.CustomerMessageData;
import com.vinod.microservices.best.practices.util.GlobalUtility;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerDeletedHandler {

    @Value("${queue.customer.deleted}")
    private String queueCustomerDeleted;

    private ObjectMapper objectMapper= GlobalUtility.getDateFormatObjectMapper();

    @SqsListener(value = "${queue.customer.deleted}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    void whenCustomerDeleted(String message, @Header("SenderId") String senderId) throws Exception {
        log.trace("Message received a customer is deleted message body: {}, having sender id",message, senderId);
        if(GlobalUtility.isExists(message)){
            CustomerMessageData receivedCustomerObject=objectMapper.readValue(message, CustomerMessageData.class);
            log.trace("Customer deleted: {}",receivedCustomerObject);
            //TODO: Code to take necessary actions.
        } else {
            log.warn("No message found in the queue: {} and the message: {}"+ queueCustomerDeleted, message);
            //TODO: Code to take necessary actions.
        }
    }
}
