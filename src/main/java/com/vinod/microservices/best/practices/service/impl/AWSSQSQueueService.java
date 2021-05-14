package com.vinod.microservices.best.practices.service.impl;

import com.vinod.microservices.best.practices.service.IQueueService;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AWSSQSQueueService implements IQueueService {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Override
    public <T> void sendMessage(String queue, T message) {
        log.trace("Request came to send message to SQS queue: {} and the message is: {}",queue, message);
        queueMessagingTemplate.convertAndSend(queue, message);
    }
}
