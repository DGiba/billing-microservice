package org.alfabet.exercise.persistence;

import org.alfabet.exercise.domain.Bill;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends ListCrudRepository<Bill, Long> {
}
