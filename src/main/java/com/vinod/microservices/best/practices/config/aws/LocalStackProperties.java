package com.vinod.microservices.best.practices.config.aws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties("localstack")
@Data
public class LocalStackProperties {
    private URI endpointURI;
}
