package org.alfabet.exercise.integrations.processor.rest;

import org.alfabet.exercise.api.processor.rest.Direction;
import org.alfabet.exercise.api.processor.rest.TransactionStatus;
import org.alfabet.exercise.exceptions.ProcessorRestClientException;

import java.util.Map;

public interface ProcessorRestClient {

    /**
     * Performing the transaction
     *
     * @param srcBankAccount source bank account
     * @param dstBankAccount destination bank account
     * @param amount         amount of money to transfer
     * @param direction      credit/debit
     * @return the transaction ID
     */
    String performTransaction(String srcBankAccount, String dstBankAccount, double amount, Direction direction) throws ProcessorRestClientException;

    /**
     * Downloading daily report
     * @return Map of transaction statuses
     */
    Map<String, TransactionStatus> downloadReport() throws ProcessorRestClientException;
}
