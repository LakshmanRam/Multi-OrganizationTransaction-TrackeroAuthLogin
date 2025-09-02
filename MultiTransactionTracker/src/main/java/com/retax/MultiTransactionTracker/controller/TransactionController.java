package com.retax.MultiTransactionTracker.controller;

import com.retax.MultiTransactionTracker.dto.TransactionDto;
import com.retax.MultiTransactionTracker.model.Transaction;
import com.retax.MultiTransactionTracker.model.User;
import com.retax.MultiTransactionTracker.security.UserPrincipal;
import com.retax.MultiTransactionTracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orgs/{orgId}/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@PathVariable Long orgId,
                                              @Valid @RequestBody TransactionDto dto,
                                              @AuthenticationPrincipal Object principal) {
        User user = extractUser(principal);
        return ResponseEntity.ok(service.createTransaction(orgId, user, dto));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long orgId,
                                                  @AuthenticationPrincipal Object principal) {
        User user = extractUser(principal);
        return ResponseEntity.ok(service.listOfTransactions(orgId, user));
    }

    @GetMapping("/transactions")
    public Page<Transaction> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return service.getSortedTransaction(page,size,sortBy,direction);
    }

    @GetMapping("/{txId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long orgId, @PathVariable Long txId,
                                           @AuthenticationPrincipal Object principal) {
        User user = extractUser(principal);
        return ResponseEntity.ok(service.getTransaction(txId, user));
    }

    @PutMapping("/{txId}")
    public ResponseEntity<Transaction> update(@PathVariable Long orgId, @PathVariable Long txId,
                                              @Valid @RequestBody TransactionDto dto,
                                              @AuthenticationPrincipal Object principal) {
        User user = extractUser(principal);
        return ResponseEntity.ok(service.updateTransaction(txId, user, dto));
    }

    @DeleteMapping("/{txId}")
    public ResponseEntity<Void> delete(@PathVariable Long orgId, @PathVariable Long txId,
                                       @AuthenticationPrincipal Object principal) {
        User user = extractUser(principal);
        service.deleteTransaction(txId, user);
        return ResponseEntity.noContent().build();
    }

    private User extractUser(Object principal) {
        if (principal instanceof User) return (User) principal;
        if (principal instanceof UserPrincipal) return ((UserPrincipal) principal).getUser();
        throw new IllegalStateException("Unsupported principal type: " + principal.getClass());
    }
}
