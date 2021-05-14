package com.vinod.microservices.best.practices.handler;

import com.fasterxml.jackson.databind.JsonNode;
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
public class CustomerCreatedHandler {

    @Value("${queue.customer.created}")
    private String eventQueueCustomerCreated;

    private ObjectMapper objectMapper= GlobalUtility.getDateFormatObjectMapper();

    @SqsListener(value = "${queue.customer.created}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    void whenCustomerCreated(String message, @Header("SenderId") String senderId) throws Exception {
        log.trace("Received message a customer created: {}, having sender id",message, senderId);
        JsonNode jsonMessage= GlobalUtility.isExists(objectMapper.readTree(message)) ? objectMapper.readTree(message).get("Message"):null;
        if(GlobalUtility.isExists(jsonMessage)){
            CustomerMessageData receivedCustomerObject=objectMapper.readValue(jsonMessage.asText(), CustomerMessageData.class);
            log.info("Received customer created object: {}",receivedCustomerObject);
            //TODO: Code to take necessary actions.
        } else {
            log.warn("No message found in the message for: {} and the message: {}"+ eventQueueCustomerCreated, message);
            //TODO: Code to take necessary actions.
        }
    }
}
