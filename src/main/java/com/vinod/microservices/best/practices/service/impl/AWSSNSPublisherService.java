package com.vinod.microservices.best.practices.service.impl;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.vinod.microservices.best.practices.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.vinod.microservices.best.practices.util.GlobalUtility.convertObjectToJson;


@Service
@Log4j2
public class AWSSNSPublisherService implements IPublisherService {

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    @Override
    public <T> String publish(String topicName, T message) {
        log.trace("Request came to publish message to :{} with subject: {}",topicName);
        try{
            PublishRequest publishRequest=new PublishRequest(amazonSNSClient.createTopic(topicName).getTopicArn(),convertObjectToJson(message));
            String resultMessageId= amazonSNSClient.publish(publishRequest).getMessageId();
            log.trace("Successfully publish message to :{} message id: {}",topicName, resultMessageId);
            return resultMessageId;
        } catch (Exception exception) {
            log.trace("Error occurred while publishing message to :{}",topicName,exception);
        }
        return  null;
    }
}
