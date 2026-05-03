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
@RequestMapping("/student")
public class StudentController {

    @Autowired private UserService userService;
    @Autowired private StudentService studentService;
    @Autowired private JobPostingService jobPostingService;
    @Autowired private ApplicationService applicationService;

    private Student getCurrentStudent(Authentication auth) {
        User user = userService.findByEmail(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return studentService.getStudentByUser(user)
            .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        model.addAttribute("student", student);
        model.addAttribute("activeJobs", jobPostingService.getActiveJobPostings().size());
        model.addAttribute("myApplications", applicationService.getApplicationsByStudent(student));
        model.addAttribute("recentJobs", jobPostingService.getActiveJobPostings().stream().limit(4).toList());
        return "student/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        model.addAttribute("student", student);
        return "student/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication auth,
                                @ModelAttribute Student studentData,
                                RedirectAttributes ra) {
        Student student = getCurrentStudent(auth);
        student.setRollNumber(studentData.getRollNumber());
        student.setDepartment(studentData.getDepartment());
        student.setBatchYear(studentData.getBatchYear());
        student.setCgpa(studentData.getCgpa());
        student.setSkills(studentData.getSkills());
        student.setLinkedinUrl(studentData.getLinkedinUrl());
        student.setGithubUrl(studentData.getGithubUrl());
        student.setAddress(studentData.getAddress());
        student.setResumeUrl(studentData.getResumeUrl());
        studentService.saveStudent(student);
        ra.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/student/profile";
    }

    @GetMapping("/jobs")
    public String listJobs(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        model.addAttribute("jobs", jobPostingService.getActiveJobPostings());
        model.addAttribute("student", student);
        // Track which jobs student has applied to
        model.addAttribute("appliedJobIds",
            applicationService.getApplicationsByStudent(student)
                .stream().map(a -> a.getJobPosting().getId()).toList());
        return "student/jobs";
    }

    @GetMapping("/jobs/{id}")
    public String viewJob(@PathVariable Long id, Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        JobPosting job = jobPostingService.getJobPostingById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));
        model.addAttribute("job", job);
        model.addAttribute("hasApplied", applicationService.hasApplied(student, job));
        return "student/job-detail";
    }

    @PostMapping("/jobs/{id}/apply")
    public String applyForJob(@PathVariable Long id,
                              @RequestParam(required = false) String coverLetter,
                              Authentication auth,
                              RedirectAttributes ra) {
        Student student = getCurrentStudent(auth);
        JobPosting job = jobPostingService.getJobPostingById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationService.hasApplied(student, job)) {
            ra.addFlashAttribute("errorMessage", "You have already applied for this job!");
            return "redirect:/student/jobs/" + id;
        }

        Application app = new Application();
        app.setStudent(student);
        app.setJobPosting(job);
        app.setCoverLetter(coverLetter);
        app.setStatus(Application.ApplicationStatus.APPLIED);
        applicationService.saveApplication(app);
        ra.addFlashAttribute("successMessage", "Application submitted successfully!");
        return "redirect:/student/applications";
    }

    @GetMapping("/applications")
    public String myApplications(Authentication auth, Model model) {
        Student student = getCurrentStudent(auth);
        var applications = applicationService.getApplicationsByStudent(student);
        model.addAttribute("applications", applications);
        model.addAttribute("totalApplications", applications.size());
        model.addAttribute("shortlistedCount",
            applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.SHORTLISTED).count());
        model.addAttribute("interviewCount",
            applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.INTERVIEW_SCHEDULED).count());
        model.addAttribute("selectedCount",
            applications.stream().filter(a -> a.getStatus() == Application.ApplicationStatus.SELECTED).count());
        return "student/applications";
    }
}
