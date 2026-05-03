package com.placement.controller;

import com.placement.model.User;
import com.placement.service.CompanyService;
import com.placement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("companies", companyService.getActiveCompanies());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        // Validate password manually (min 6 chars) since we removed @Size from entity
        if (user.getPassword() == null || user.getPassword().trim().length() < 6) {
            model.addAttribute("errorMessage", "Password must be at least 6 characters.");
            return "auth/register";
        }

        // Validate role is selected
        if (user.getRole() == null) {
            model.addAttribute("errorMessage", "Please select a role.");
            model.addAttribute("companies", companyService.getActiveCompanies());
            return "auth/register";
        }

        if (user.getRole() == User.Role.RECRUITER &&
            (user.getCompany() == null || user.getCompany().getId() == null)) {
            model.addAttribute("errorMessage", "Please select your company.");
            model.addAttribute("companies", companyService.getActiveCompanies());
            return "auth/register";
        }

        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.getActiveCompanies());
            return "auth/register";
        }

        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please login with your credentials.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
