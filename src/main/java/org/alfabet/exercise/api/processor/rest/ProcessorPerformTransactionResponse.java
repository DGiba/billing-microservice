package org.alfabet.exercise.api.processor.rest;

import lombok.Getter;

import java.util.Map;

@Getter
public class ProcessorPerformTransactionResponse {
    Map<String, TransactionStatus> report;
}
