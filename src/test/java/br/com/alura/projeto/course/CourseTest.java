package br.com.alura.projeto.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CourseTest {
    @Test
    @DisplayName("should create course with valid data and set status as ACTIVE by default")
    void shouldCreateCourseWithValidDataAndSetStatusAsActiveByDefault() {
        String name = "Spring Boot Advanced";
        String code = "spring-boot-avancado";
        String instructor = "João Silva";
        String category = "Backend";
        String description = "Advanced Spring Boot course";

        Course course = new Course(name, code, instructor, category, description);

        assertThat(course.getName()).isEqualTo(name);
        assertThat(course.getCode()).isEqualTo(code);
        assertThat(course.getInstructor()).isEqualTo(instructor);
        assertThat(course.getCategory()).isEqualTo(category);
        assertThat(course.getDescription()).isEqualTo(description);
        assertThat(course.getStatus()).isEqualTo(CourseStatus.ACTIVE);
        assertThat(course.getInactivationDate()).isNull();
        assertThat(course.isActive()).isTrue();
    }

    @Test
    @DisplayName("should accept valid code with letters and hyphens")
    void shouldAcceptValidCodeWithLettersAndHyphens() {
        String validCode = "spring-boot-avancado";

        Course course = new Course("Test Course", validCode, "Instructor", "Category", "Description");

        assertThat(course.getCode()).isEqualTo(validCode);
        assertThat(course.getStatus()).isEqualTo(CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should inactivate course and set inactivation date")
    void shouldInactivateCourseAndSetInactivationDate() {
        Course course = new Course("Test Course", "test-course", "Instructor", "Category", "Description");
        LocalDateTime beforeInactivation = LocalDateTime.now();

        course.inactivate();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.INACTIVE);
        assertThat(course.getInactivationDate()).isNotNull();
        assertThat(course.getInactivationDate()).isAfterOrEqualTo(beforeInactivation);
        assertThat(course.isActive()).isFalse();
    }

    @Test
    @DisplayName("should activate course and clear inactivation date")
    void shouldActivateCourseAndClearInactivationDate() {
        Course course = new Course("Test Course", "test-course", "Instructor", "Category", "Description");
        course.inactivate();

        course.activate();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.ACTIVE);
        assertThat(course.getInactivationDate()).isNull();
        assertThat(course.isActive()).isTrue();
    }

    @Test
    @DisplayName("should reject code with numbers")
    void shouldRejectCodeWithNumbers() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Test Course", "code123", "Instructor", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Code must contain only letters and hyphens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject code too short")
    void shouldRejectCodeTooShort() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Test Course", "abc", "Instructor", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Code must be between 4 and 10 characters")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with blank name")
    void shouldRejectCourseWithBlankName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("", "valid-code", "Instructor", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Name is required")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with blank instructor")
    void shouldRejectCourseWithBlankInstructor() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Valid Name", "valid-code", "", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("instructor") && 
                              v.getMessage().contains("Instructor is required")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with blank category")
    void shouldRejectCourseWithBlankCategory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Valid Name", "valid-code", "Instructor", "", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("category") && 
                              v.getMessage().contains("Category is required")))
                .isTrue();
    }
}
