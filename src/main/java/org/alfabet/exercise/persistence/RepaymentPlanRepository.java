package org.alfabet.exercise.persistence;

import org.alfabet.exercise.domain.RepaymentPlan;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepaymentPlanRepository extends ListCrudRepository<RepaymentPlan, Long> {
}
