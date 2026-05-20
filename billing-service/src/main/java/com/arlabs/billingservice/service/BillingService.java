package com.arlabs.billingservice.service;

import com.arlabs.billingservice.model.BillingAccount;
import com.arlabs.billingservice.model.BillingTransaction;
import com.arlabs.billingservice.repository.BillingAccountRepository;
import com.arlabs.billingservice.repository.BillingTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingAccountRepository billingAccountRepository;
    private final BillingTransactionRepository billingTransactionRepository;

    public BillingAccount getAccountByPatientId(UUID patientId) {
        return billingAccountRepository.findByPatientId(patientId);
    }

    @Transactional
    public BillingTransaction addCharge(UUID patientId, BigDecimal amount, String description) {

        BillingAccount account = billingAccountRepository.findByPatientId(patientId);
        if(account==null) {
            throw new RuntimeException("Billing account not found for patient: " + patientId);
        }
        account.setBalance(account.getBalance().add(amount));
        billingAccountRepository.save(account);

        BillingTransaction charge = BillingTransaction.builder()
                .account(account)
                .amount(amount)
                .description(description)
                .type(BillingTransaction.TransactionType.CHARGE)
                .build();

        return billingTransactionRepository.save(charge);
    }


    @Transactional
    public BillingTransaction recordPayment(UUID patientId, BigDecimal amount, String description) {
        BillingAccount account = billingAccountRepository.findByPatientId(patientId);
        if(account==null) {
            throw new RuntimeException("Billing account not found for patient: " + patientId);
        }

        account.setBalance(account.getBalance().subtract(amount));
        billingAccountRepository.save(account);

        BillingTransaction payment = BillingTransaction.builder()
                .account(account)
                .amount(amount)
                .description(description)
                .type(BillingTransaction.TransactionType.PAYMENT)
                .build();

        return billingTransactionRepository.save(payment);
    }


}
