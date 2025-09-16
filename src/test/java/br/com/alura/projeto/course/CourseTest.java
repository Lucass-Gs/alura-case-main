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
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
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
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
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
                              v.getMessage().contains("Nome é obrigatório")))
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
                              v.getMessage().contains("Instrutor é obrigatório")))
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
                              v.getMessage().contains("Categoria é obrigatória")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with name too long")
    void shouldRejectCourseWithNameTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String longName = "A".repeat(101); // 101 characters
        Course course = new Course(longName, "valid-code", "Instructor", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome não pode exceder 100 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with instructor name too long")
    void shouldRejectCourseWithInstructorNameTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String longInstructor = "A".repeat(101); // 101 characters
        Course course = new Course("Valid Name", "valid-code", longInstructor, "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("instructor") && 
                              v.getMessage().contains("Nome do instrutor não pode exceder 100 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with category too long")
    void shouldRejectCourseWithCategoryTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String longCategory = "A".repeat(51); // 51 characters
        Course course = new Course("Valid Name", "valid-code", "Instructor", longCategory, "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("category") && 
                              v.getMessage().contains("Categoria não pode exceder 50 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject course with description too long")
    void shouldRejectCourseWithDescriptionTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String longDescription = "A".repeat(501); // 501 characters
        Course course = new Course("Valid Name", "valid-code", "Instructor", "Category", longDescription);

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description") && 
                              v.getMessage().contains("Descrição não pode exceder 500 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept course with null description")
    void shouldAcceptCourseWithNullDescription() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Valid Name", "valid-code", "Instructor", "Category", null);

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept course with empty description")
    void shouldAcceptCourseWithEmptyDescription() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Valid Name", "valid-code", "Instructor", "Category", "");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should reject code with spaces")
    void shouldRejectCodeWithSpaces() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Test Course", "code with spaces", "Instructor", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject code with special characters")
    void shouldRejectCodeWithSpecialCharacters() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Course course = new Course("Test Course", "code@#$", "Instructor", "Category", "Description");

        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept valid codes with different patterns")
    void shouldAcceptValidCodesWithDifferentPatterns() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String[] validCodes = {"java", "spring-boot", "react-js", "node-js", "python-django"};
        
        for (String code : validCodes) {
            Course course = new Course("Test Course", code, "Instructor", "Category", "Description");
            Set<ConstraintViolation<Course>> violations = validator.validate(course);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")))
                    .as("Code '%s' should be valid", code)
                    .isFalse();
        }
    }
}
