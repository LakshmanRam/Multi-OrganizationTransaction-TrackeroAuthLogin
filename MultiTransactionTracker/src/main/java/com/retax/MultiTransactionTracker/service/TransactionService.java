package com.retax.MultiTransactionTracker.service;

import com.retax.MultiTransactionTracker.Exceptions.NotFoundException;
import com.retax.MultiTransactionTracker.dto.TransactionDto;
import com.retax.MultiTransactionTracker.model.Organization;
import com.retax.MultiTransactionTracker.model.Transaction;
import com.retax.MultiTransactionTracker.model.User;
import com.retax.MultiTransactionTracker.repository.OrganizationRepository;
import com.retax.MultiTransactionTracker.repository.TransactionRepository;
import org.hibernate.annotations.TypeRegistrations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OrganizationRepository organizationRepository;

    public TransactionService(TransactionRepository transactionRepository, OrganizationRepository organizationRepository) {
        this.transactionRepository = transactionRepository;
        this.organizationRepository = organizationRepository;
    }
@Transactional
    public Transaction createTransaction(Long orgId, User user, TransactionDto dto) {
        Organization org = validateOrganizationByOrgId(orgId, user);
        Transaction transaction = new Transaction();
        transaction.setOrganization(org);
        transaction.setType(dto.getType());
        transaction.setAmount(dto.getAmount());
        transaction.setItem(dto.getItem());
        transaction.setDate(dto.getDate() !=null ? dto.getDate() : Instant.now());
        return transactionRepository.save(transaction);
    }
    public List<Transaction> listOfTransactions(Long orgId, User user) {
        Organization organizationByOrgId = validateOrganizationByOrgId(orgId, user);
//        return transactionRepository.findByOrganization(organizationByOrgId);
        return transactionRepository.findByOrganizationId(organizationByOrgId.getId());
    }
    @Transactional
    public Transaction updateTransaction(Long txId, User user, TransactionDto dto) {
        Transaction transaction = getTransaction(txId, user);
        transaction.setType(dto.getType());
        transaction.setAmount(dto.getAmount());
        transaction.setItem(dto.getItem());
        transaction.setDate(dto.getDate());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long txId, User user) {
        Transaction transactionDelete = getTransaction(txId, user);
        transactionRepository.delete(transactionDelete);
    }
    private Organization validateOrganizationByOrgId(Long orgId, User user) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        if (!organization.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Organization not found With User or access denied");
        }
        return organization;
    }
    public Transaction getTransaction(Long txId, User user)  {
        Transaction transaction = transactionRepository.findById(txId).orElseThrow(() -> new NotFoundException("Transaction not found"));
        if (!transaction.getOrganization().getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Transaction not found or access denied");
        }
        return transaction;
    }
    public Page<Transaction> getSortedTransaction(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
return transactionRepository.findAll(pageable);
    }
}
