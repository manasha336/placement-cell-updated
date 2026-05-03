package com.placement.service;

import com.placement.model.Company;
import com.placement.model.JobPosting;
import com.placement.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    private void expirePastDeadlines() {
        List<JobPosting> activeJobs = jobPostingRepository.findByStatus(JobPosting.JobStatus.ACTIVE);
        LocalDate today = LocalDate.now();
        List<JobPosting> expiredJobs = activeJobs.stream()
            .filter(job -> job.getApplicationDeadline() != null && job.getApplicationDeadline().isBefore(today))
            .peek(job -> job.setStatus(JobPosting.JobStatus.CLOSED))
            .toList();
        if (!expiredJobs.isEmpty()) {
            jobPostingRepository.saveAll(expiredJobs);
        }
    }

    public List<JobPosting> getAllJobPostings() {
        expirePastDeadlines();
        return jobPostingRepository.findAll();
    }

    public List<JobPosting> getJobPostingsByCompany(Company company) {
        expirePastDeadlines();
        return jobPostingRepository.findByCompany(company);
    }

    public List<JobPosting> getActiveJobPostingsByCompany(Company company) {
        expirePastDeadlines();
        return jobPostingRepository.findByCompanyAndStatus(company, JobPosting.JobStatus.ACTIVE);
    }

    public List<JobPosting> getActiveJobPostings() {
        expirePastDeadlines();
        return jobPostingRepository.findByStatus(JobPosting.JobStatus.ACTIVE);
    }

    public Optional<JobPosting> getJobPostingById(Long id) {
        expirePastDeadlines();
        return jobPostingRepository.findById(id);
    }

    public JobPosting saveJobPosting(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }

    public long countActiveJobs() {
        expirePastDeadlines();
        return jobPostingRepository.countByStatus(JobPosting.JobStatus.ACTIVE);
    }
}
