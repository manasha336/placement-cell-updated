package com.placement.repository;

import com.placement.model.Student;
import com.placement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByRollNumber(String rollNumber);
    List<Student> findByDepartment(String department);
    List<Student> findByPlacementStatus(Student.PlacementStatus status);
    long countByPlacementStatus(Student.PlacementStatus status);
    
    @Query("SELECT DISTINCT s.department FROM Student s")
    List<String> findDistinctDepartments();
}
