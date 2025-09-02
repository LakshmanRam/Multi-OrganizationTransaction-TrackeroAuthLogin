package com.retax.MultiTransactionTracker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponseDto {
    private Long id;
    private String name;
    private String description;
//    private Long userId; // optionally include owner ID
}

