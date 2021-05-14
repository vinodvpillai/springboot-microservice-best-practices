package com.vinod.microservices.best.practices.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * Internationalization Utility Class.
 */
@Component
public class ResourceMessage {

    private static ResourceBundleMessageSource messageSource;

    @Autowired
    ResourceMessage(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String getMessage(String msgCode) {
        return messageSource.getMessage(msgCode, null, LocaleContextHolder.getLocale());
    }
}
