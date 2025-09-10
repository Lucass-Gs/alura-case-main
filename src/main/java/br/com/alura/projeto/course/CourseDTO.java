package br.com.alura.projeto.course;

import java.time.LocalDateTime;

public record CourseDTO(Long id, String name, String code, String instructor, String category, 
                       String description, CourseStatus status, LocalDateTime createdAt, 
                       LocalDateTime inactivationDate) {
    
    public CourseDTO(Course course) {
        this(course.getId(), course.getName(), course.getCode(), course.getInstructor(), 
             course.getCategory(), course.getDescription(), course.getStatus(), 
             course.getCreatedAt(), course.getInactivationDate());
    }
}