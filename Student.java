package com.placement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "roll_number", unique = true)
    private String rollNumber;

    @Column(name = "department")
    private String department;

    @Column(name = "batch_year")
    private Integer batchYear;

    @Column(name = "cgpa")
    private Double cgpa;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "placement_status")
    private PlacementStatus placementStatus = PlacementStatus.UNPLACED;

    @Column(name = "placed_company")
    private String placedCompany;

    @Column(name = "package_lpa")
    private Double packageLpa;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (placementStatus == null) placementStatus = PlacementStatus.UNPLACED;
    }

    public enum PlacementStatus {
        PLACED, UNPLACED, IN_PROCESS
    }
}
