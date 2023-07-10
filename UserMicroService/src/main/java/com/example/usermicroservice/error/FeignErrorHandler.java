package com.example.usermicroservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
@Component
@RequiredArgsConstructor
public class FeignErrorHandler implements ErrorDecoder {
    private final Environment environment;
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()){
            case 400:
                break;
            case 404:
                if(s.contains("getOrders")) {
                    return new ResponseStatusException(HttpStatusCode.valueOf(response.status()),
                            environment.getProperty("order_service.exception.order_is_empty"));
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
