package org.alfabet.exercise.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfabet.exercise.persistence.RepaymentRepository;
import org.alfabet.exercise.services.renewal.RenewalService;
import org.alfabet.exercise.services.repayment.RepaymentService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {
    private final RepaymentService repaymentService;
    private final RenewalService renewalService;
    private final RepaymentRepository repaymentRepository;

    /**
     * Scheduled process (every day)
     * Checks if there are any open RepaymentPlan and initiate the repayment process if the day has come
     */
    @Scheduled(cron = "${alfabet.exercise.payment-scheduler-cron}")
    @Transactional
    public void pay() {
        log.info("Scheduled payment started");

        var todayRepayments = repaymentRepository.findAllByDate(LocalDate.now());

        if (todayRepayments.isEmpty()) {
            log.info("No repayments are planned for today");
            return;
        }

        todayRepayments.forEach(repayment -> {
            try {
                repaymentService.repay(repayment);
            } catch (Exception e) {
                log.error("Error occurred while trying to process repayment='{}'", repayment.getId(), e);
            }
        });

        renewalService.renew();

        log.info("Scheduled payment finished");
    }
}
