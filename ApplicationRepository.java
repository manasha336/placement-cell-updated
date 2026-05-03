package com.placement.repository;

import com.placement.model.Application;
import com.placement.model.Company;
import com.placement.model.JobPosting;
import com.placement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(Student student);
    List<Application> findByJobPosting(JobPosting jobPosting);
    Optional<Application> findByStudentAndJobPosting(Student student, JobPosting jobPosting);
    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);
    List<Application> findByJobPosting_Company(Company company);
    long countByJobPosting_Company(Company company);
    long countByStatus(Application.ApplicationStatus status);
}
