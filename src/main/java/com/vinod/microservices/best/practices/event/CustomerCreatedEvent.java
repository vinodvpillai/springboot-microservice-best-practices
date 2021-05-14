package com.vinod.microservices.best.practices.event;

import com.vinod.microservices.best.practices.dto.CustomerMessageData;
import com.vinod.microservices.best.practices.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerCreatedEvent {

    @Autowired
    private IPublisherService publisherEvent;

    @Value("${event.topic.customer.created}")
    private String eventCustomerCreated;

    public void on(CustomerMessageData msgData) {
        log.trace("Request came to raise event for customer created: {}",msgData);
        try{
            publisherEvent.publish(eventCustomerCreated, msgData);
            log.info("Successfully publish message for customer created: {}",msgData);
        } catch (Exception e) {
            log.error("Error occurred while raising event for customer created: {}",msgData,e);
        }
    }
}
