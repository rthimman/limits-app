package com.stellantis.securitization.interceptor;

import com.stellantis.securitization.exception.MissingMandatoryFieldException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class PathParamValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        @SuppressWarnings("unchecked")
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVars == null) return true;

        validate(pathVars, "countryCode");
        validate(pathVars, "fundCode");

        if (pathVars.containsKey("criteriaCode")) {
            validate(pathVars, "criteriaCode");
        }

        return true;

    }

    private void validate(Map<String, String> pathVars, String field) {
        String value = pathVars.get(field);
        if (value == null || value.trim().isEmpty()) {
            throw new MissingMandatoryFieldException(field + " is required");
        }
    }
}
