package com.retax.MultiTransactionTracker.controller;

import com.retax.MultiTransactionTracker.OrganizationResponseDto;
import com.retax.MultiTransactionTracker.dto.OrganizationDto;
import com.retax.MultiTransactionTracker.model.Organization;
import com.retax.MultiTransactionTracker.model.User;
import com.retax.MultiTransactionTracker.security.UserPrincipal;
import com.retax.MultiTransactionTracker.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrganizationResponseDto> createOrg(@Valid @RequestBody OrganizationDto dto,
                                                             @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        OrganizationResponseDto org = service.createOrganization(user, dto);

        return ResponseEntity.ok(org);
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getOrgs(@AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.getListOfOrganizations(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrgsById(@PathVariable Long id,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.getOrganizationForUser(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrgsById(@PathVariable Long id,
                                               @Valid @RequestBody OrganizationDto dto,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.updateOrganization(id, user, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrgsById(@PathVariable Long id,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        service.deleteOrganization(id, user);
        return ResponseEntity.noContent().build();
    }

}
