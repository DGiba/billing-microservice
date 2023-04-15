package org.alfabet.exercise.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_id")
    @SequenceGenerator(name = "bill_id", allocationSize = 1)
    private long id;
    @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "bill")
    private RepaymentPlan repaymentPlan;
    @Column(name = "customer_bank_account")
    private String customerBankAccount;
    private double amount;
    @Column(name = "credit_transaction_id")
    private String creditTransactionId;

    public Bill(String customerBankAccount, double amount) {
        this.customerBankAccount = customerBankAccount;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + id +
            ", customerBankAccount='" + customerBankAccount + '\'' +
            ", amount=" + amount +
            ", creditTransactionId='" + creditTransactionId + '\'' +
            '}';
    }
}
