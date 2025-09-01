package com.nataliya.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WeatherApiResponseException extends RuntimeException {

    private final HttpStatus status;

    public WeatherApiResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
