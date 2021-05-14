package com.vinod.microservices.best.practices.service;

public interface IPublisherService {
    /**
     * Publish message.
     *
     * @param topicName     - Topic.
     * @param message       - Message.
     * @param <T>           - Message Type.
     * @return              - Result String.
     */
    <T> String publish(String topicName, T message);
}