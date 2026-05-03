package com.placement.controller;

import com.placement.model.*;
import com.placement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private StudentService studentService;
    @Autowired private CompanyService companyService;
    @Autowired private JobPostingService jobPostingService;
    @Autowired private ApplicationService applicationService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.getAllStudents().size());
        model.addAttribute("placedStudents", studentService.countPlacedStudents());
        model.addAttribute("unplacedStudents", studentService.countUnplacedStudents());
        model.addAttribute("totalCompanies", companyService.countActiveCompanies());
        model.addAttribute("activeJobs", jobPostingService.countActiveJobs());
        model.addAttribute("totalApplications", applicationService.countAllApplications());
        model.addAttribute("recentStudents", studentService.getAllStudents().stream().limit(5).toList());
        model.addAttribute("recentJobs", jobPostingService.getActiveJobPostings().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ---- STUDENTS ----
    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "admin/students";
    }

    @GetMapping("/students/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        model.addAttribute("applications", applicationService.getApplicationsByStudent(student));
        return "admin/student-detail";
    }

    @PostMapping("/students/{id}/edit")
    public String editStudent(@PathVariable Long id,
                              @RequestParam(required = false) String rollNumber,
                              @RequestParam(required = false) String department,
                              @RequestParam(required = false) Integer batchYear,
                              @RequestParam(required = false) Double cgpa,
                              @RequestParam(required = false) String skills,
                              @RequestParam(required = false) String linkedinUrl,
                              @RequestParam(required = false) String githubUrl,
                              @RequestParam(required = false) String address,
                              RedirectAttributes ra) {
        Student student = studentService.getStudentById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        if (rollNumber != null) student.setRollNumber(rollNumber);
        if (department != null) student.setDepartment(department);
        if (batchYear != null) student.setBatchYear(batchYear);
        if (cgpa != null) student.setCgpa(cgpa);
        if (skills != null) student.setSkills(skills);
        if (linkedinUrl != null) student.setLinkedinUrl(linkedinUrl);
        if (githubUrl != null) student.setGithubUrl(githubUrl);
        if (address != null) student.setAddress(address);
        studentService.saveStudent(student);
        ra.addFlashAttribute("successMessage", "Student profile updated successfully!");
        return "redirect:/admin/students/" + id;
    }

    @PostMapping("/students/{id}/status")
    public String updateStudentStatus(@PathVariable Long id,
                                      @RequestParam String status,
                                      @RequestParam(required = false) String company,
                                      @RequestParam(required = false) Double packageLpa,
                                      RedirectAttributes ra) {
        Student student = studentService.getStudentById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setPlacementStatus(Student.PlacementStatus.valueOf(status));
        if (company != null) student.setPlacedCompany(company);
        if (packageLpa != null) student.setPackageLpa(packageLpa);
        studentService.saveStudent(student);
        ra.addFlashAttribute("successMessage", "Student status updated successfully!");
        return "redirect:/admin/students/" + id;
    }

    // ---- COMPANIES ----
    @GetMapping("/companies")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("company", new Company());
        return "admin/companies";
    }

    @PostMapping("/companies/add")
    public String addCompany(@ModelAttribute Company company, RedirectAttributes ra) {
        companyService.saveCompany(company);
        ra.addFlashAttribute("successMessage", "Company added successfully!");
        return "redirect:/admin/companies";
    }

    @GetMapping("/companies/{id}/edit")
    public String editCompanyForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        model.addAttribute("editCompany", company);
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("company", new Company());
        return "admin/companies";
    }

    @PostMapping("/companies/{id}/edit")
    public String editCompany(@PathVariable Long id, @ModelAttribute Company updatedCompany, RedirectAttributes ra) {
        Company company = companyService.getCompanyById(id)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompanyName(updatedCompany.getCompanyName());
        company.setIndustry(updatedCompany.getIndustry());
        company.setWebsite(updatedCompany.getWebsite());
        company.setLocation(updatedCompany.getLocation());
        company.setContactPerson(updatedCompany.getContactPerson());
        company.setContactEmail(updatedCompany.getContactEmail());
        company.setContactPhone(updatedCompany.getContactPhone());
        company.setDescription(updatedCompany.getDescription());
        company.setIsActive(updatedCompany.getIsActive() != null ? updatedCompany.getIsActive() : true);
        companyService.saveCompany(company);
        ra.addFlashAttribute("successMessage", "Company updated successfully!");
        return "redirect:/admin/companies";
    }

    @GetMapping("/companies/{id}/delete")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes ra) {
        companyService.deleteCompany(id);
        ra.addFlashAttribute("successMessage", "Company deleted successfully!");
        return "redirect:/admin/companies";
    }

    // ---- JOB POSTINGS ----
    @GetMapping("/jobs")
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobPostingService.getAllJobPostings());
        model.addAttribute("companies", companyService.getActiveCompanies());
        model.addAttribute("job", new JobPosting());
        return "admin/jobs";
    }

    @PostMapping("/jobs/add")
    public String addJob(@ModelAttribute JobPosting job,
                         @RequestParam Long companyId,
                         RedirectAttributes ra) {
        Company company = companyService.getCompanyById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        job.setCompany(company);
        job.setStatus(JobPosting.JobStatus.ACTIVE);
        jobPostingService.saveJobPosting(job);
        ra.addFlashAttribute("successMessage", "Job posting added successfully!");
        return "redirect:/admin/jobs";
    }

    @GetMapping("/jobs/{id}/edit")
    public String editJobForm(@PathVariable Long id, Model model) {
        JobPosting job = jobPostingService.getJobPostingById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        model.addAttribute("editJob", job);
        model.addAttribute("jobs", jobPostingService.getAllJobPostings());
        model.addAttribute("companies", companyService.getActiveCompanies());
        model.addAttribute("job", new JobPosting());
        return "admin/jobs";
    }

    @PostMapping("/jobs/{id}/edit")
    public String editJob(@PathVariable Long id,
                          @ModelAttribute JobPosting updatedJob,
                          @RequestParam Long companyId,
                          RedirectAttributes ra) {
        JobPosting job = jobPostingService.getJobPostingById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        Company company = companyService.getCompanyById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        job.setCompany(company);
        job.setJobTitle(updatedJob.getJobTitle());
        job.setJobDescription(updatedJob.getJobDescription());
        job.setRequiredSkills(updatedJob.getRequiredSkills());
        job.setMinCgpa(updatedJob.getMinCgpa());
        job.setPackageLpa(updatedJob.getPackageLpa());
        job.setJobType(updatedJob.getJobType());
        job.setLocation(updatedJob.getLocation());
        job.setVacancies(updatedJob.getVacancies());
        job.setApplicationDeadline(updatedJob.getApplicationDeadline());
        job.setBatchYear(updatedJob.getBatchYear());
        job.setDepartmentEligible(updatedJob.getDepartmentEligible());
        jobPostingService.saveJobPosting(job);
        ra.addFlashAttribute("successMessage", "Job updated successfully!");
        return "redirect:/admin/jobs";
    }

    @GetMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, RedirectAttributes ra) {
        jobPostingService.deleteJobPosting(id);
        ra.addFlashAttribute("successMessage", "Job posting deleted successfully!");
        return "redirect:/admin/jobs";
    }

    @PostMapping("/jobs/{id}/status")
    public String updateJobStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes ra) {
        JobPosting job = jobPostingService.getJobPostingById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(JobPosting.JobStatus.valueOf(status));
        jobPostingService.saveJobPosting(job);
        ra.addFlashAttribute("successMessage", "Job status updated!");
        return "redirect:/admin/jobs";
    }

    // ---- APPLICATIONS ----
    @GetMapping("/applications")
    public String listApplications(Model model) {
        model.addAttribute("applications", applicationService.getAllApplications());
        return "admin/applications";
    }

    @PostMapping("/applications/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id,
                                          @RequestParam String status,
                                          RedirectAttributes ra) {
        Application app = applicationService.getApplicationById(id)
            .orElseThrow(() -> new RuntimeException("Application not found"));
        app.setStatus(Application.ApplicationStatus.valueOf(status));
        applicationService.saveApplication(app);
        ra.addFlashAttribute("successMessage", "Application status updated!");
        return "redirect:/admin/applications";
    }

    // ---- USERS ----
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/admin/users";
    }
}
