package com.example.api.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    private final ErrorAttributes errorAttributes;

    @RequestMapping(PATH)
    public ResponseEntity<ErrorDTO> error(WebRequest webRequest) {

        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE
                        )
        );

        return ResponseEntity
                .status((Integer) attributes.getOrDefault("status", 500))
                .body(ErrorDTO.builder()
                        .error((String) attributes.getOrDefault("error", "Internal Server Error"))
                        .errorDescription((String) attributes.getOrDefault("message", "Unexpected error"))
                        .build()
                );
    }
}
