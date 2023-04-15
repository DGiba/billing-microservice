package org.alfabet.exercise.services.billing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfabet.exercise.ApplicationProperties;
import org.alfabet.exercise.api.processor.rest.Direction;
import org.alfabet.exercise.domain.Bill;
import org.alfabet.exercise.domain.Repayment;
import org.alfabet.exercise.domain.RepaymentPlan;
import org.alfabet.exercise.exceptions.ProcessorRestClientException;
import org.alfabet.exercise.integrations.processor.rest.ProcessorRestClient;
import org.alfabet.exercise.persistence.BillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultBillingService implements BillingService {
    private final ProcessorRestClient processorRestClient;
    private final BillRepository billRepository;
    private final ApplicationProperties properties;

    @Transactional
    public void performAdvance(String dstBankAccount, double amount) throws ProcessorRestClientException {
        log.info("Starting the billing service for bankAccount='{}' with amount='{}'", dstBankAccount, amount);

        var bill = new Bill(dstBankAccount, amount);
        var repaymentPlan = createRepaymentPlan(amount / 12);
        repaymentPlan.setBill(bill);
        bill.setRepaymentPlan(repaymentPlan);

        String transactionId = processorRestClient.performTransaction(properties.getCreditBankAccount(),
            dstBankAccount, amount, Direction.CREDIT);

        bill.setCreditTransactionId(transactionId);
        billRepository.save(bill);

        log.info("Th billing process for bankAccount='{}' with amount='{}' successfully finished", dstBankAccount, amount);
    }

    //==================================================================================================================
    //= Implementation
    //==================================================================================================================

    private RepaymentPlan createRepaymentPlan(double weeklyDebitAmount) {
        var currentDate = LocalDate.now();
        var repaymentPlan = new RepaymentPlan(currentDate);

        List<Repayment> repayments = new LinkedList<>();
        for (int i = 1; i <= 12; i++) {
            repayments.add(new Repayment(currentDate.plusWeeks(i), weeklyDebitAmount, repaymentPlan));
        }
        repaymentPlan.setRepayments(repayments);

        return repaymentPlan;
    }
}
