package org.alfabet.exercise.services.renewal;

public interface RenewalService {

    /**
     * Checks the RepaymentPlans and renew the repayment process if previous one failed
     */
    void renew();
}
