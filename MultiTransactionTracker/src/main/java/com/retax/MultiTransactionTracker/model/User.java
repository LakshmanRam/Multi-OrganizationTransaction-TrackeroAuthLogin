package com.retax.MultiTransactionTracker.model;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String pictureUrl;
    private Instant createdAt;
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<Organization> organizations;
    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }
}