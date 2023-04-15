package org.alfabet.exercise.services.repayment;

import org.alfabet.exercise.domain.Repayment;
import org.alfabet.exercise.exceptions.ProcessorRestClientException;

public interface RepaymentService {

    /**
     * Initiate weekly repayment process of RepaymentPlan
     *
     * @param repayment repayment to process
     */
    void repay(Repayment repayment) throws ProcessorRestClientException;
}
