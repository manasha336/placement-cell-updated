package com.placement.config;

import com.placement.model.Company;
import com.placement.model.JobPosting;
import com.placement.model.Student;
import com.placement.model.User;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobPostingRepository;
import com.placement.repository.StudentRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private JobPostingRepository jobPostingRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Create Admin user
        if (!userRepository.existsByEmail("admin@placement.com")) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@placement.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            admin.setPhone("9876543210");
            admin.setIsActive(true);
            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@placement.com / admin123");
        }

        // Create demo Student
        if (!userRepository.existsByEmail("student@placement.com")) {
            User studentUser = new User();
            studentUser.setFirstName("Ravi");
            studentUser.setLastName("Kumar");
            studentUser.setEmail("student@placement.com");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setRole(User.Role.STUDENT);
            studentUser.setPhone("9123456789");
            studentUser.setIsActive(true);
            User saved = userRepository.save(studentUser);

            Student student = new Student();
            student.setUser(saved);
            student.setRollNumber("CS2021001");
            student.setDepartment("Computer Science");
            student.setBatchYear(2025);
            student.setCgpa(8.5);
            student.setSkills("Java, Spring Boot, MySQL, Python");
            student.setPlacementStatus(Student.PlacementStatus.UNPLACED);
            studentRepository.save(student);
            System.out.println("✅ Student user created: student@placement.com / student123");
        }

        // Create demo Companies and Jobs
        if (companyRepository.count() == 0) {
            String[][] companies = {
                {"TCS", "IT Services", "https://tcs.com", "Mumbai", "hr@tcs.com", "+91-22-67789999"},
                {"Infosys", "IT Services", "https://infosys.com", "Bangalore", "hr@infosys.com", "+91-80-22042000"},
                {"Wipro", "IT Services", "https://wipro.com", "Bangalore", "hr@wipro.com", "+91-80-28440011"},
                {"Google", "Technology", "https://google.com", "Hyderabad", "hr@google.com", "+91-40-66252000"},
                {"Amazon", "E-Commerce", "https://amazon.com", "Bangalore", "hr@amazon.com", "+91-80-49429800"}
            };

            double[] packages = {6.5, 8.0, 7.5, 18.0, 14.0};
            int idx = 0;
            for (String[] c : companies) {
                Company company = new Company();
                company.setCompanyName(c[0]);
                company.setIndustry(c[1]);
                company.setWebsite(c[2]);
                company.setLocation(c[3]);
                company.setContactEmail(c[4]);
                company.setContactPhone(c[5]);
                company.setIsActive(true);
                company.setDescription("Leading " + c[1] + " company offering exciting opportunities.");
                Company saved = companyRepository.save(company);

                // Add a job posting for each company
                JobPosting job = new JobPosting();
                job.setCompany(saved);
                job.setJobTitle("Software Engineer");
                job.setJobDescription("We are hiring talented software engineers to join our growing team. You will work on cutting-edge technologies and build scalable solutions.");
                job.setRequiredSkills("Java, Spring Boot, SQL, REST APIs");
                job.setMinCgpa(7.0);
                job.setPackageLpa(packages[idx]);
                job.setJobType("Full-Time");
                job.setLocation(c[3]);
                job.setVacancies(10);
                job.setApplicationDeadline(LocalDate.now().plusDays(30));
                job.setBatchYear(2025);
                job.setDepartmentEligible("All Departments");
                job.setStatus(JobPosting.JobStatus.ACTIVE);
                jobPostingRepository.save(job);
                idx++;
            }
            System.out.println("✅ Sample companies and job postings created");
        }

        // Create demo Recruiter and assign company
        if (!userRepository.existsByEmail("recruiter@placement.com")) {
            User recruiterUser = new User();
            recruiterUser.setFirstName("HR");
            recruiterUser.setLastName("Manager");
            recruiterUser.setEmail("recruiter@placement.com");
            recruiterUser.setPassword(passwordEncoder.encode("recruiter123"));
            recruiterUser.setRole(User.Role.RECRUITER);
            recruiterUser.setPhone("9988776655");
            recruiterUser.setIsActive(true);

            Company assignedCompany = companyRepository.findByCompanyName("TCS")
                    .orElseGet(() -> companyRepository.findByIsActiveTrue().stream().findFirst().orElse(null));
            recruiterUser.setCompany(assignedCompany);

            userRepository.save(recruiterUser);
            System.out.println("✅ Recruiter user created: recruiter@placement.com / recruiter123");
        }

        System.out.println("============================================");
        System.out.println("  🚀 Placement Cell App is Ready!");
        System.out.println("  URL: http://localhost:8080");
        System.out.println("  Admin:     admin@placement.com / admin123");
        System.out.println("  Student:   student@placement.com / student123");
        System.out.println("  Recruiter: recruiter@placement.com / recruiter123");
        System.out.println("============================================");
    }
}
