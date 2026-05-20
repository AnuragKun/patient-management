package com.arlabs.billingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private UUID patientId;
    private BigDecimal balance;
    private String status;
    private List<TransactionResponse> transactions;
}
