package com.placement.service;

import com.placement.model.Company;
import com.placement.model.Student;
import com.placement.model.User;
import com.placement.repository.StudentRepository;
import com.placement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CompanyService companyService;

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        if (user.getRole() == User.Role.RECRUITER) {
            if (user.getCompany() == null || user.getCompany().getId() == null) {
                throw new RuntimeException("Recruiter company is required.");
            }
            Company company = companyService.getCompanyById(user.getCompany().getId())
                    .orElseThrow(() -> new RuntimeException("Selected company not found."));
            user.setCompany(company);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Create student profile if role is STUDENT
        if (user.getRole() == User.Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            studentRepository.save(student);
        }

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public long countStudents() {
        return userRepository.countByRole(User.Role.STUDENT);
    }
}
