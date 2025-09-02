package com.retax.MultiTransactionTracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)

    private Type type;


    private BigDecimal amount;


    private String item;


//    private Instant transactionDate;

    private Instant date;

    @ManyToOne(optional = false)
    private Organization organization;


    public enum Type { SALE, PURCHASE }
    @PrePersist
    public void prePersist() { if (date == null) date = Instant.now(); }

}
