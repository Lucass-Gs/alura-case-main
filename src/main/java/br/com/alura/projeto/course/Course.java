package br.com.alura.projeto.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Code is required")
    @Size(min = 4, max = 10, message = "Code must be between 4 and 10 characters")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Code must contain only letters and hyphens, no spaces, numbers or special characters")
    @Column(name = "code", nullable = false, unique = true, length = 10)
    @EqualsAndHashCode.Include
    private String code;

    @NotBlank(message = "Instructor is required")
    @Size(max = 100, message = "Instructor name must not exceed 100 characters")
    @Column(name = "instructor", nullable = false, length = 100)
    private String instructor;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseStatus status = CourseStatus.ACTIVE;

    @Column(name = "inactivation_date")
    private LocalDateTime inactivationDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Course(String name, String code, String instructor, String category, String description) {
        this.name = name;
        this.code = code;
        this.instructor = instructor;
        this.category = category;
        this.description = description;
    }

    public void inactivate() {
        this.status = CourseStatus.INACTIVE;
        this.inactivationDate = LocalDateTime.now();
    }

    public void activate() {
        this.status = CourseStatus.ACTIVE;
        this.inactivationDate = null;
    }

    public boolean isActive() {
        return this.status == CourseStatus.ACTIVE;
    }
}