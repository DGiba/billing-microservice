package org.alfabet.exercise.exceptions;

import org.springframework.web.client.RestClientResponseException;

public class ProcessorRestClientException extends RestClientResponseException {

    public ProcessorRestClientException(String operation, RestClientResponseException exception) {
        super(
            "Error occurred while trying to send '" + operation + "' request",
            exception.getStatusCode(), exception.getStatusText(),
            exception.getResponseHeaders(), exception.getResponseBodyAsByteArray(), null
        );
    }
}
