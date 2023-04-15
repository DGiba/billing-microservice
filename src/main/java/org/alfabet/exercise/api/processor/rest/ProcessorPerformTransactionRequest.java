package org.alfabet.exercise.api.processor.rest;

import lombok.Data;

@Data
public class ProcessorPerformTransactionRequest {
    private String srcBankAccount;
    private String dstBankAccount;
    private double amount;
    private Direction direction;
}
