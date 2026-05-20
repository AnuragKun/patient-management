package com.arlabs.billingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String description;
    private String type;
    private LocalDateTime createdAt;
}
