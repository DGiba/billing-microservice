package org.alfabet.exercise.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfabet.exercise.services.billing.BillingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@Slf4j
public class RestController {
    private final BillingService billingService;

    @GetMapping("/performAdvance")
    public void performAdvance(@RequestParam String dstBankAccount, @RequestParam double amount) {
        log.info("Starting the advance performance for bankAccount='{}'", dstBankAccount);

        try {
            billingService.performAdvance(dstBankAccount, amount);
        } catch (Exception e) {
            log.error("Advance performance failed with an ERROR upon integration with 'Processor'", e);
        }

        log.info("Advance performance finished for bankAccount='{}'", dstBankAccount);
    }
}
