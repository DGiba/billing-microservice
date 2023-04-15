package org.alfabet.exercise.services.renewal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfabet.exercise.api.processor.rest.TransactionStatus;
import org.alfabet.exercise.domain.Repayment;
import org.alfabet.exercise.domain.RepaymentPlan;
import org.alfabet.exercise.integrations.processor.rest.ProcessorRestClient;
import org.alfabet.exercise.persistence.RepaymentPlanRepository;
import org.alfabet.exercise.persistence.RepaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultRenewalService implements RenewalService {
    private final ProcessorRestClient processorRestClient;
    private final RepaymentPlanRepository repaymentPlanRepository;
    private final RepaymentRepository repaymentRepository;

    @Override
    public void renew() {
        log.info("Renewal process started");

        var transactionStatuses = processorRestClient.downloadReport();
        if (transactionStatuses == null || transactionStatuses.isEmpty()) {
            log.info("New transaction are not represented");
            return;
        }

        var repaymentPlans = repaymentPlanRepository.findAll();

        repaymentPlans.forEach(repaymentPlan -> {
                if (repaymentPlan.getRepayments().isEmpty()) {
                    log.info("No repayments left for RepaymentPlan='{}'. Deleting the plan", repaymentPlan.getId());
                    repaymentPlanRepository.delete(repaymentPlan);
                    return;
                }
                repaymentPlan.getRepayments().sort(Comparator.comparing(Repayment::getDate));
                if (repaymentPlan.getRepayments().get(0).getTransactionId() != null) {
                    var payedRepayment = repaymentPlan.getRepayments().remove(0);
                    repaymentRepository.delete(payedRepayment);

                    if (transactionStatuses.get(payedRepayment.getTransactionId()).equals(TransactionStatus.FAIL)) {
                        log.info("Last repayment with id='{}' was failed. Planing new repayment", payedRepayment.getId());
                        queueNewRepayment(payedRepayment, repaymentPlan);
                    }
                }
            }
        );

        log.info("Renewal process finished");
    }

    //==================================================================================================================
    //= Implementation
    //==================================================================================================================

    private void queueNewRepayment(Repayment payedRepayment, RepaymentPlan repaymentPlan) {
        var amountOfRepaymentsLeft = repaymentPlan.getRepayments().size();
        var newDate = payedRepayment.getDate().plusWeeks(amountOfRepaymentsLeft + 1);

        var newRepayment = new Repayment();
        newRepayment.setAmount(payedRepayment.getAmount());
        newRepayment.setRepaymentPlan(repaymentPlan);
        newRepayment.setDate(newDate);

        repaymentPlan.getRepayments().add(newRepayment);
    }
}
