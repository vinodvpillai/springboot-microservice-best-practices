package com.vinod.microservices.best.practices.service;

public interface IQueueService {

    /**
     * Send message to Queue.
     *
     * @param queue     -   Queue name.
     * @param message   -   Message.
     */
    public <T> void sendMessage(String queue, T message);
}
