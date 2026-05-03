package com.placement.repository;

import com.placement.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByIsActiveTrue();
    List<Company> findByIndustry(String industry);
    Optional<Company> findByCompanyName(String companyName);
    long countByIsActiveTrue();
}
