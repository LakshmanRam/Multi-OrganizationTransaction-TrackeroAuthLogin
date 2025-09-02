package com.retax.MultiTransactionTracker.model;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.Instant;


@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    @Column(columnDefinition = "text")
    private String description;


    private Instant createdAt = Instant.now();


    private Instant updatedAt = Instant.now();


    @ManyToOne(optional = false)
    @JsonIgnore
    private User owner;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); updatedAt = createdAt; }

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }

}
