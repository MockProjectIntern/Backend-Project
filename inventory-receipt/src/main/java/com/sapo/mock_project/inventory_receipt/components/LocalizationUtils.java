package com.sapo.mock_project.inventory_receipt.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * This class provides utility methods for localization in the application.
 * It uses Spring's MessageSource and LocaleResolver to resolve messages
 * based on the current locale.
 */
@RequiredArgsConstructor
@Component
public class LocalizationUtils {

    /**
     * The MessageSource to fetch localized messages from.
     */
    private final MessageSource messageSource;

    /**
     * The LocaleResolver to determine the current locale from the request.
     */
    private final LocaleResolver localeResolver;

    /**
     * Retrieves a localized message based on the provided message key and parameters.
     *
     * @param messageKey the key for the message to be localized
     * @param params     optional parameters to be included in the message
     * @return the localized message as a String
     */
    public String getLocalizedMessage(String messageKey, Object... params) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, params, locale);
    }
}
