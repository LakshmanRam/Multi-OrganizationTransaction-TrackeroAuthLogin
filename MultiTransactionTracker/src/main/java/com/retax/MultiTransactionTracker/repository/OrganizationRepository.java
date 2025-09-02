package com.retax.MultiTransactionTracker.repository;

import com.retax.MultiTransactionTracker.model.Organization;
import com.retax.MultiTransactionTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    List<Organization> findByOwner(User owner);

}
