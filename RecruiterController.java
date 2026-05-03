package com.placement.controller;

import com.placement.model.*;
import com.placement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {

    @Autowired private JobPostingService jobPostingService;
    @Autowired private ApplicationService applicationService;
    @Autowired private CompanyService companyService;
    @Autowired private StudentService studentService;
    @Autowired private UserService userService;

    private Company getCurrentRecruiterCompany(Authentication auth) {
        User user = userService.findByEmail(auth.getName())
            .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        if (user.getCompany() == null) {
            throw new RuntimeException("Recruiter is not assigned to a company.");
        }
        return user.getCompany();
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Company company = getCurrentRecruiterCompany(auth);
        var activeJobs = jobPostingService.getActiveJobPostingsByCompany(company);
        model.addAttribute("company", company);
        model.addAttribute("activeJobs", activeJobs);
        model.addAttribute("totalApplications", applicationService.countApplicationsByCompany(company));
        model.addAttribute("recentJobs", activeJobs.stream().limit(5).toList());
        return "recruiter/dashboard";
    }

    @GetMapping("/jobs")
    public String listJobs(Authentication auth, Model model) {
        Company company = getCurrentRecruiterCompany(auth);
        model.addAttribute("jobs", jobPostingService.getJobPostingsByCompany(company));
        model.addAttribute("company", company);
        return "recruiter/jobs";
    }

    @GetMapping("/jobs/{id}/applications")
    public String viewApplications(@PathVariable Long id, Authentication auth, Model model) {
        Company company = getCurrentRecruiterCompany(auth);
        JobPosting job = jobPostingService.getJobPostingById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        if (!job.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Access denied: job does not belong to your company.");
        }
        model.addAttribute("job", job);
        model.addAttribute("applications", applicationService.getApplicationsByJob(job));
        return "recruiter/applications";
    }

    @PostMapping("/applications/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) Long jobId,
                               RedirectAttributes ra) {
        Application app = applicationService.getApplicationById(id)
            .orElseThrow(() -> new RuntimeException("Application not found"));
        app.setStatus(Application.ApplicationStatus.valueOf(status));
        applicationService.saveApplication(app);
        ra.addFlashAttribute("successMessage", "Application status updated!");
        if (jobId != null) {
            return "redirect:/recruiter/jobs/" + jobId + "/applications";
        }
        return "redirect:/recruiter/dashboard";
    }
}
