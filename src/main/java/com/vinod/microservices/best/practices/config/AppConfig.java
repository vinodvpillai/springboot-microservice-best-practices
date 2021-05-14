package com.vinod.microservices.best.practices.config;

import com.vinod.microservices.best.practices.service.IPublisherService;
import com.vinod.microservices.best.practices.service.IQueueService;
import com.vinod.microservices.best.practices.service.impl.AWSSNSPublisherService;
import com.vinod.microservices.best.practices.service.impl.AWSSQSQueueService;
import com.vinod.microservices.best.practices.service.impl.RabbitMQPublisherService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    private Environment environment;

    public AppConfig(Environment environment) {
        this.environment=environment;
    }

    /**
     * Model mapper.
     *
     * @return the model mapper
     */
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Publisher Service - Object base on the property value.
     * Vendor neutral program.
     *
     * @param context
     * @return
     */
    @Bean(name = "publisherEvent")
    public IPublisherService publisherEvent(ApplicationContext context) {
        if (environment.getProperty("message.event").equals("AWS_SNS")) {
            return context.getBean(AWSSNSPublisherService.class);
        } else {
            return context.getBean(RabbitMQPublisherService.class);
        }
    }

    /**
     * Queue Service - Object base on the property value.
     * Vendor neutral program.
     *
     * @param context
     * @return
     */
    @Bean(name = "queueService")
    public IQueueService queueService(ApplicationContext context) {
        if (environment.getProperty("message.queue").equals("AWS_SQS")) {
            return context.getBean(AWSSQSQueueService.class);
        } else {
            return null;
        }
    }
}
