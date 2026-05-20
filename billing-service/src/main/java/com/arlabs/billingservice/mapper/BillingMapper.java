package com.arlabs.billingservice.mapper;

import com.arlabs.billingservice.dto.AccountResponse;
import com.arlabs.billingservice.dto.TransactionResponse;
import com.arlabs.billingservice.model.BillingAccount;
import com.arlabs.billingservice.model.BillingTransaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillingMapper {

    public AccountResponse mapToAccountResponse(BillingAccount account) {
        List<TransactionResponse> transactionResponses = account.getTransactions()
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());

        return AccountResponse.builder()
                .id(account.getId())
                .patientId(account.getPatientId())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .transactions(transactionResponses)
                .build();
    }

    public TransactionResponse mapToTransactionResponse(BillingTransaction transaction) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .type(transaction.getType().name())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
