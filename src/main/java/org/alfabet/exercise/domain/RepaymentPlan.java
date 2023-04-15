package org.alfabet.exercise.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class RepaymentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repayment_plan_id")
    @SequenceGenerator(name = "repayment_plan_id", allocationSize = 1)
    private long id;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "repaymentPlan")
    List<Repayment> repayments;
    @OneToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;
    @Column(name = "credit_date")
    LocalDate creditDate;

    public RepaymentPlan(LocalDate creditDate) {
        this.creditDate = creditDate;
    }

    @Override
    public String toString() {
        return "RepaymentPlan{" +
            "id=" + id +
            ", bill=" + bill +
            ", creditDate=" + creditDate +
            '}';
    }
}
