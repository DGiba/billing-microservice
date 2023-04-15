package org.alfabet.exercise.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Repayment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repayment_id")
    @SequenceGenerator(name = "repayment_id", allocationSize = 1)
    private long id;
    private LocalDate date;
    private double amount;
    @ManyToOne
    @JoinColumn(name = "repayment_plan_id")
    private RepaymentPlan repaymentPlan;
    @Column(name = "transaction_id")
    private String transactionId;

    public Repayment(LocalDate date, double amount, RepaymentPlan repaymentPlan) {
        this.date = date;
        this.amount = amount;
        this.repaymentPlan = repaymentPlan;
    }


    @Override
    public String toString() {
        return "Repayment{" +
            "id=" + id +
            ", date=" + date +
            ", amount=" + amount +
            ", transactionId='" + transactionId + '\'' +
            '}';
    }
}



