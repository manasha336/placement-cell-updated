package com.placement.service;

import com.placement.model.Student;
import com.placement.model.User;
import com.placement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByUser(User user) {
        return studentRepository.findByUser(user);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }

    public List<Student> getStudentsByStatus(Student.PlacementStatus status) {
        return studentRepository.findByPlacementStatus(status);
    }

    public long countPlacedStudents() {
        return studentRepository.countByPlacementStatus(Student.PlacementStatus.PLACED);
    }

    public long countUnplacedStudents() {
        return studentRepository.countByPlacementStatus(Student.PlacementStatus.UNPLACED);
    }

    public List<String> getAllDepartments() {
        return studentRepository.findDistinctDepartments();
    }
}
