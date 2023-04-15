package org.alfabet.exercise.integrations.processor.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfabet.exercise.api.processor.rest.Direction;
import org.alfabet.exercise.api.processor.rest.ProcessorPerformTransactionRequest;
import org.alfabet.exercise.api.processor.rest.ProcessorPerformTransactionResponse;
import org.alfabet.exercise.api.processor.rest.TransactionStatus;
import org.alfabet.exercise.exceptions.ProcessorRestClientException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultProcessorRestClient implements ProcessorRestClient {
    private final RestTemplate restTemplate;

    @Override
    public String performTransaction(String srcBankAccount, String dstBankAccount, double amount, Direction direction) throws ProcessorRestClientException {
        log.info("Starting the transaction from='{}' to='{}'", srcBankAccount, dstBankAccount);

        ProcessorPerformTransactionRequest request = createRequest(srcBankAccount, dstBankAccount, amount, direction);
        ResponseEntity<String> response;

        try {
            response = restTemplate.postForEntity("/performTransaction", request, String.class);
        } catch (RestClientResponseException e) {
            throw new ProcessorRestClientException("performTransaction", e);
        }

        log.info("The transactions from='{}' to='{}' finished", srcBankAccount, dstBankAccount);

        return response.getBody();
    }

    @Override
    public Map<String, TransactionStatus> downloadReport() throws ProcessorRestClientException {
        log.info("Downloading daily report");

        ResponseEntity<ProcessorPerformTransactionResponse> response;
        try {
            response = restTemplate.getForEntity("/downloadReport", ProcessorPerformTransactionResponse.class);
        } catch (RestClientResponseException e) {
            throw new ProcessorRestClientException("downloadReport", e);
        }


        if (response.hasBody()) {
            log.info("Daily report downloaded");

            return response.getBody().getReport();
        } else {
            log.info("Empty daily report downloaded");

            return null;
        }
    }


    //==================================================================================================================
    //= Implementation
    //==================================================================================================================

    private ProcessorPerformTransactionRequest createRequest(String srcBankAccount, String dstBankAccount,
                                                             double amount, Direction direction) {
        ProcessorPerformTransactionRequest request = new ProcessorPerformTransactionRequest();
        request.setSrcBankAccount(srcBankAccount);
        request.setDstBankAccount(dstBankAccount);
        request.setAmount(amount);
        request.setDirection(direction);
        return request;
    }
}
