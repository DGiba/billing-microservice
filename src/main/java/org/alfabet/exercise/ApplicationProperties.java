package org.alfabet.exercise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("alfabet.exercise")
@Data
public class ApplicationProperties {
    String creditBankAccount;
    String paymentSchedulerRate;
}
