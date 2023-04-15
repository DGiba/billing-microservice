package org.alfabet.exercise.services.repayment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfabet.exercise.ApplicationProperties;
import org.alfabet.exercise.api.processor.rest.Direction;
import org.alfabet.exercise.domain.Repayment;
import org.alfabet.exercise.exceptions.ProcessorRestClientException;
import org.alfabet.exercise.integrations.processor.rest.ProcessorRestClient;
import org.alfabet.exercise.persistence.RepaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultRepaymentService implements RepaymentService {
    private final ProcessorRestClient processorRestClient;
    private final ApplicationProperties applicationProperties;
    private final RepaymentRepository repaymentRepository;

    @Override
    public void repay(Repayment repayment) throws ProcessorRestClientException {
        log.info("Starting repayment process for repayment='{}'", repayment.getId());

        var customerBankAccount = repayment.getRepaymentPlan().getBill().getCustomerBankAccount();

        var transactionId = processorRestClient.performTransaction(
            customerBankAccount,
            applicationProperties.getCreditBankAccount(),
            repayment.getAmount(),
            Direction.DEBIT
        );

        repayment.setTransactionId(transactionId);
        repaymentRepository.save(repayment);

        log.info("Repayment process for repayment='{}' finished", repayment.getId());
    }
}
