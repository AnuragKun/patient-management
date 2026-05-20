package com.arlabs.billingservice.controller;

import com.arlabs.billingservice.dto.AccountResponse;
import com.arlabs.billingservice.dto.TransactionRequest;
import com.arlabs.billingservice.dto.TransactionResponse;
import com.arlabs.billingservice.mapper.BillingMapper;
import com.arlabs.billingservice.model.BillingAccount;
import com.arlabs.billingservice.model.BillingTransaction;
import com.arlabs.billingservice.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;
    private final BillingMapper billingMapper;

    @GetMapping("/{patientId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID patientId) {
        BillingAccount account = billingService.getAccountByPatientId(patientId);

        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(billingMapper.mapToAccountResponse(account));
    }


    @PostMapping("/{patientId}/charge")
    public ResponseEntity<TransactionResponse> addCharge(@PathVariable UUID patientId, @Valid @RequestBody TransactionRequest transactionRequest) {

        BillingTransaction transaction = billingService.addCharge(patientId,transactionRequest.getAmount(), transactionRequest.getDescription());
        return ResponseEntity.ok(billingMapper.mapToTransactionResponse(transaction));
    }


    @PostMapping("/{patientId}/payment")
    public ResponseEntity<TransactionResponse> recordPayment(@PathVariable UUID patientId, @Valid @RequestBody TransactionRequest transactionRequest) {

        BillingTransaction transaction = billingService.recordPayment(patientId,transactionRequest.getAmount(), transactionRequest.getDescription());
        return ResponseEntity.ok(billingMapper.mapToTransactionResponse(transaction));
    }
}
