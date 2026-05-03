package com.placement.service;

import com.placement.model.Application;
import com.placement.model.Company;
import com.placement.model.JobPosting;
import com.placement.model.Student;
import com.placement.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByStudent(Student student) {
        return applicationRepository.findByStudent(student);
    }

    public List<Application> getApplicationsByJob(JobPosting jobPosting) {
        return applicationRepository.findByJobPosting(jobPosting);
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByCompany(Company company) {
        return applicationRepository.findByJobPosting_Company(company);
    }

    public long countApplicationsByCompany(Company company) {
        return applicationRepository.countByJobPosting_Company(company);
    }

    public boolean hasApplied(Student student, JobPosting jobPosting) {
        return applicationRepository.existsByStudentAndJobPosting(student, jobPosting);
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public long countByStatus(Application.ApplicationStatus status) {
        return applicationRepository.countByStatus(status);
    }

    public long countAllApplications() {
        return applicationRepository.count();
    }
}
