package com.arlabs.billingservice.repository;

import com.arlabs.billingservice.model.BillingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingTransactionRepository extends JpaRepository<BillingTransaction, Long> {
    List<BillingTransaction> findByAccountId(Long accountId);
}
