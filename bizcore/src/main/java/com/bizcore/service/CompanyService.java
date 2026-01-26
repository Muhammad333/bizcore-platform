package com.bizcore.service;

import com.bizcore.dto.CompanyDTO;
import com.bizcore.entity.Company;
import com.bizcore.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findByActiveTrue().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyById(Long id) {
        return companyRepository.findById(id)
            .map(this::convertToDTO)
            .orElse(null);
    }

    public CompanyDTO getCompanyByCode(String code) {
        return companyRepository.findByCode(code)
            .map(this::convertToDTO)
            .orElse(null);
    }

    public CompanyDTO createCompany(CompanyDTO dto) {
        if (companyRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Company with code " + dto.getCode() + " already exists");
        }

        Company company = new Company();
        updateCompanyFromDTO(company, dto);
        company.setActive(true);
        company.setSubscriptionPlan("FREE");

        Company saved = companyRepository.save(company);
        return convertToDTO(saved);
    }

    public CompanyDTO updateCompany(Long id, CompanyDTO dto) {
        return companyRepository.findById(id)
            .map(company -> {
                updateCompanyFromDTO(company, dto);
                Company saved = companyRepository.save(company);
                return convertToDTO(saved);
            })
            .orElse(null);
    }

    public boolean deleteCompany(Long id) {
        return companyRepository.findById(id)
            .map(company -> {
                company.setActive(false);
                companyRepository.save(company);
                return true;
            })
            .orElse(false);
    }

    private void updateCompanyFromDTO(Company company, CompanyDTO dto) {
        company.setCode(dto.getCode());
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setLogo(dto.getLogo());
        company.setEmail(dto.getEmail());
        company.setPhone(dto.getPhone());
        company.setAddress(dto.getAddress());
        company.setCity(dto.getCity());
        company.setCountry(dto.getCountry());
        company.setPostalCode(dto.getPostalCode());
        company.setTaxId(dto.getTaxId());
        company.setWebsite(dto.getWebsite());
    }

    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setCode(company.getCode());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setLogo(company.getLogo());
        dto.setEmail(company.getEmail());
        dto.setPhone(company.getPhone());
        dto.setAddress(company.getAddress());
        dto.setCity(company.getCity());
        dto.setCountry(company.getCountry());
        dto.setPostalCode(company.getPostalCode());
        dto.setTaxId(company.getTaxId());
        dto.setWebsite(company.getWebsite());
        dto.setActive(company.isActive());
        dto.setSubscriptionPlan(company.getSubscriptionPlan());
        dto.setSubscriptionExpiresAt(company.getSubscriptionExpiresAt());
        dto.setUserCount(company.getUsers() != null ? company.getUsers().size() : 0);
        dto.setCreatedAt(company.getCreatedAt());
        dto.setUpdatedAt(company.getUpdatedAt());
        return dto;
    }
}
