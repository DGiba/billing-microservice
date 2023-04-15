package org.alfabet.exercise.services.billing;

import org.alfabet.exercise.exceptions.ProcessorRestClientException;

public interface BillingService {

    /**
     * Advance performance
     * Crediting bank account with specified amount of money
     *
     * @param dstBankAccount destination bank account
     * @param amount         amount of credit
     */
    void performAdvance(String dstBankAccount, double amount) throws ProcessorRestClientException;
}
