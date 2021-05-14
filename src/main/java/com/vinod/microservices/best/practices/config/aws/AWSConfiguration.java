package com.vinod.microservices.best.practices.config.aws;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Log4j2
public class AWSConfiguration {

    private Environment environment;

    public AWSConfiguration(Environment environment) {
        this.environment=environment;
    }

    /**
     * AWS SQS Messaging queue message template.
     *
     * @param amazonSQSAsync
     * @param localStackProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "message", name = "queue", havingValue = "AWS_SQS")
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync, LocalStackProperties localStackProperties) {
        if(localStackProperties.getEndpointURI() !=null) {
            amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard().withEndpointConfiguration(getEndpoint(localStackProperties)).build();
            return new QueueMessagingTemplate(amazonSQSAsync);
        }
        amazonSQSAsync = AmazonSQSAsyncClientBuilder.defaultClient();
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    /**
     * AWS SNS Notification message template.
     *
     * @param amazonSNS
     * @param localStackProperties
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "message", name = "event", havingValue = "AWS_SNS")
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS,LocalStackProperties localStackProperties) {
        if(localStackProperties.getEndpointURI() !=null) {
            amazonSNS = AmazonSNSClientBuilder.standard().withEndpointConfiguration(getEndpoint(localStackProperties)).build();
            return new NotificationMessagingTemplate(amazonSNS);
        }
        amazonSNS = AmazonSNSClientBuilder.defaultClient();
        return new NotificationMessagingTemplate(amazonSNS);
    }

    /**
     * Setting the Endpoint URL - LocalStack.
     *
     * @param localStackProperties
     * @return
     */
    private AwsClientBuilder.EndpointConfiguration getEndpoint(LocalStackProperties localStackProperties) {
        if(localStackProperties.getEndpointURI() !=null) {
            return new AwsClientBuilder.EndpointConfiguration(localStackProperties.getEndpointURI().toString(), "Region.AF_SOUTH_1");
        }
        return null;
    }

    /**
     * Get the credentials from the Environment variable.
     * Also can be used case when we are having both access key and secret and test from local.
     *
     * @return - AWSCredentials object.
     */
    private AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(environment.getProperty("AWS_ACCESS_KEY_ID"), environment.getProperty("AWS_SECRET_ACCESS_KEY"));
    }

}
