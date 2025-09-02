package com.retax.MultiTransactionTracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {

    @NotBlank
    private String name;
    private String description;

}
