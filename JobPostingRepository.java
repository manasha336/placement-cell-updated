package com.placement.repository;

import com.placement.model.Company;
import com.placement.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByStatus(JobPosting.JobStatus status);
    List<JobPosting> findByCompany(Company company);
    List<JobPosting> findByCompanyAndStatus(Company company, JobPosting.JobStatus status);
    long countByStatus(JobPosting.JobStatus status);
}
