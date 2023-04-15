package org.alfabet.exercise.persistence;

import org.alfabet.exercise.domain.Repayment;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepaymentRepository extends ListCrudRepository<Repayment, Long> {

    /**
     * Find all repayments, which are planned on specified date
     *
     * @param date planned date of repayment
     * @return list of repayments
     */
    List<Repayment> findAllByDate(LocalDate date);
}
