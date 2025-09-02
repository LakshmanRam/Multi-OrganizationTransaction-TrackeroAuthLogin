package com.retax.MultiTransactionTracker.dto;

import com.retax.MultiTransactionTracker.model.Transaction;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @NotNull
    private Transaction.Type type;
    @NotNull
    private BigDecimal amount;
    private String item;
    @NotNull
    private Instant date;

}

