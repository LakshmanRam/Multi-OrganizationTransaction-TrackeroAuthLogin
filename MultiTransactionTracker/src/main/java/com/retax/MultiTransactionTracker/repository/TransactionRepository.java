package com.retax.MultiTransactionTracker.repository;

import com.retax.MultiTransactionTracker.model.Organization;
import com.retax.MultiTransactionTracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByOrganizationId(Long id);
}
