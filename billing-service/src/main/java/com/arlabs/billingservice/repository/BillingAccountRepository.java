package com.arlabs.billingservice.repository;

import com.arlabs.billingservice.model.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingAccountRepository extends JpaRepository<BillingAccount, Long> {
    BillingAccount findByPatientId(UUID patientId);
}
