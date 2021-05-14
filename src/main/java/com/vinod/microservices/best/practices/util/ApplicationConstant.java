package com.vinod.microservices.best.practices.util;

public final class ApplicationConstant {

    /**
     * Instantiates a new application constant.
     */
    private ApplicationConstant() {
    }

    //AWS - Parameter & Secret Property
    public static final String ENABLED = "awsParameterStore.enabled";
    public static final String HALT_BOOT_CONFIGURATION_PROPERTY = "awsParameterStore.haltBoot";
    public static final String PROPERTY_SOURCE_NAME = "AWSParameterStore";
    public static final String SECRET_STORE_ENABLED_CONFIGURATION_PROPERTY = "awsSecretStore.enabled";
    public static final String SECRET_STORE_PROPERTY_SOURCE_NAME = "AWSSecretStorePropertySource";

    //Endpoint
    public static final String CUSTOMER_SERVICE = "/v1/customers";


    /**
     * The Enum Customer Status.
     */
    public enum CustomerStatus {
        PENDING("Pending"),
        REGISTERED("Registered"),
        BLOCKED("Blocked");

        /** The value. */
        private final String value;

        CustomerStatus(final String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
