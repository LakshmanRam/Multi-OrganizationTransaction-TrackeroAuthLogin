package com.retax.MultiTransactionTracker.service;

import com.retax.MultiTransactionTracker.Exceptions.NotFoundException;
import com.retax.MultiTransactionTracker.OrganizationResponseDto;
import com.retax.MultiTransactionTracker.dto.OrganizationDto;
import com.retax.MultiTransactionTracker.model.Organization;
import com.retax.MultiTransactionTracker.model.User;
import com.retax.MultiTransactionTracker.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public OrganizationResponseDto createOrganization(User owner, OrganizationDto dto) {
        Organization organization = new Organization();
        organization.setName(dto.getName());
        organization.setDescription(dto.getDescription());
        organization.setOwner(owner); // assumes Organization has owner field
//        organization.setCreatedAt(Instant.now());
//        organization.setUpdatedAt(Instant.now());
        Organization saved = organizationRepository.save(organization);
        OrganizationResponseDto Responcedto = new OrganizationResponseDto();
        Responcedto.setName(organization.getName());
        Responcedto.setDescription(organization.getDescription());
        Responcedto.setId(organization.getId());
        return Responcedto;
    }

    public List<Organization> getListOfOrganizations(User owner) {
        return organizationRepository.findByOwner(owner);
    }

    public Organization getOrganizationForUser(Long orgId, User user){

        return organizationRepository.findById(orgId)
                .filter(o -> o.getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> new NotFoundException("Organization not found or access denied"));
    }
    public Organization updateOrganization(Long orgId, User user, OrganizationDto dto){
        Organization forUser = getOrganizationForUser(orgId, user);
        forUser.setName(dto.getName());
        forUser.setDescription(dto.getDescription());
        forUser.setUpdatedAt(Instant.now());
        return organizationRepository.save(forUser);
    }

    public void deleteOrganization(Long orgId, User user) throws NotFoundException {
        Organization org = getOrganizationForUser(orgId, user);
        organizationRepository.delete(org);
    }
}
