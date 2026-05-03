package com.placement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_postings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "min_cgpa")
    private Double minCgpa;

    @Column(name = "package_lpa")
    private Double packageLpa;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "location")
    private String location;

    @Column(name = "vacancies")
    private Integer vacancies;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "batch_year")
    private Integer batchYear;

    @Column(name = "department_eligible")
    private String departmentEligible;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JobStatus status = JobStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = JobStatus.ACTIVE;
    }

    public enum JobStatus {
        ACTIVE, CLOSED, DRAFT
    }
}
