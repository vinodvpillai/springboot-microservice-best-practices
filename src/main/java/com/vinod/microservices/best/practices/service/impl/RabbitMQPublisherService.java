package com.vinod.microservices.best.practices.service.impl;

import com.vinod.microservices.best.practices.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RabbitMQPublisherService implements IPublisherService {

    @Override
    public <T> String publish(String topicName, T message) {
        log.info("Request came to publish the following msg: {} to the topic: {}", message, topicName);

        //TODO: Logic to send msg base on the RabbitMQ

        return null;
    }
}
