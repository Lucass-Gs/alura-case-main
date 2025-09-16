package br.com.alura.projeto.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EditCourseFormTest {

    private Validator validator;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        testCourse = new Course("Spring Boot", "spring", "João Silva", "Backend", "Spring Boot course");
        testCourse.setId(1L);
        testCourse.setStatus(CourseStatus.ACTIVE);
        testCourse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("should create form from course with all fields")
    void shouldCreateFormFromCourseWithAllFields() {

        EditCourseForm form = new EditCourseForm(testCourse);


        assertThat(form.getName()).isEqualTo("Spring Boot");
        assertThat(form.getCode()).isEqualTo("spring");
        assertThat(form.getInstructor()).isEqualTo("João Silva");
        assertThat(form.getCategory()).isEqualTo("Backend");
        assertThat(form.getDescription()).isEqualTo("Spring Boot course");
        assertThat(form.getStatus()).isEqualTo(CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should create form from course with null description")
    void shouldCreateFormFromCourseWithNullDescription() {

        testCourse.setDescription(null);


        EditCourseForm form = new EditCourseForm(testCourse);


        assertThat(form.getName()).isEqualTo("Spring Boot");
        assertThat(form.getCode()).isEqualTo("spring");
        assertThat(form.getInstructor()).isEqualTo("João Silva");
        assertThat(form.getCategory()).isEqualTo("Backend");
        assertThat(form.getDescription()).isNull();
        assertThat(form.getStatus()).isEqualTo(CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should update course with valid data")
    void shouldUpdateCourseWithValidData() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot Advanced");
        form.setCode("spring-advanced");
        form.setInstructor("Maria Santos");
        form.setCategory("Backend");
        form.setDescription("Advanced Spring Boot course");
        form.setStatus(CourseStatus.ACTIVE);


        form.updateCourse(testCourse);


        assertThat(testCourse.getName()).isEqualTo("Spring Boot Advanced");
        assertThat(testCourse.getCode()).isEqualTo("spring-advanced");
        assertThat(testCourse.getInstructor()).isEqualTo("Maria Santos");
        assertThat(testCourse.getCategory()).isEqualTo("Backend");
        assertThat(testCourse.getDescription()).isEqualTo("Advanced Spring Boot course");
        assertThat(testCourse.getStatus()).isEqualTo(CourseStatus.ACTIVE);
    }

    @Test
    @DisplayName("should activate course when status is ACTIVE")
    void shouldActivateCourseWhenStatusIsActive() {

        testCourse.setStatus(CourseStatus.INACTIVE);
        testCourse.setInactivationDate(LocalDateTime.now());
        
        EditCourseForm form = new EditCourseForm();
        form.setStatus(CourseStatus.ACTIVE);


        form.updateCourse(testCourse);


        assertThat(testCourse.getStatus()).isEqualTo(CourseStatus.ACTIVE);
        assertThat(testCourse.getInactivationDate()).isNull();
    }

    @Test
    @DisplayName("should inactivate course when status is INACTIVE")
    void shouldInactivateCourseWhenStatusIsInactive() {

        testCourse.setStatus(CourseStatus.ACTIVE);
        testCourse.setInactivationDate(null);
        
        EditCourseForm form = new EditCourseForm();
        form.setStatus(CourseStatus.INACTIVE);


        form.updateCourse(testCourse);


        assertThat(testCourse.getStatus()).isEqualTo(CourseStatus.INACTIVE);
        assertThat(testCourse.getInactivationDate()).isNotNull();
    }

    @Test
    @DisplayName("should reject form with blank name")
    void shouldRejectFormWithBlankName() {

        EditCourseForm form = new EditCourseForm();
        form.setName("");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank code")
    void shouldRejectFormWithBlankCode() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank instructor")
    void shouldRejectFormWithBlankInstructor() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("instructor") && 
                              v.getMessage().contains("Instrutor é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank category")
    void shouldRejectFormWithBlankCategory() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("category") && 
                              v.getMessage().contains("Categoria é obrigatória")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with invalid code format")
    void shouldRejectFormWithInvalidCodeFormat() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring123");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too short")
    void shouldRejectFormWithCodeTooShort() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("ab");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too long")
    void shouldRejectFormWithCodeTooLong() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("very-very-very-long-code");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with name too long")
    void shouldRejectFormWithNameTooLong() {

        EditCourseForm form = new EditCourseForm();
        form.setName("A".repeat(101)); // 101 characters
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome não pode exceder 100 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with instructor name too long")
    void shouldRejectFormWithInstructorNameTooLong() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("A".repeat(101)); // 101 characters
        form.setCategory("Backend");
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("instructor") && 
                              v.getMessage().contains("Nome do instrutor não pode exceder 100 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with category too long")
    void shouldRejectFormWithCategoryTooLong() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("A".repeat(51)); // 51 characters
        form.setDescription("Description");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("category") && 
                              v.getMessage().contains("Categoria não pode exceder 50 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with description too long")
    void shouldRejectFormWithDescriptionTooLong() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("A".repeat(501)); // 501 characters
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description") && 
                              v.getMessage().contains("Descrição não pode exceder 500 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept form with null description")
    void shouldAcceptFormWithNullDescription() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription(null);
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept form with empty description")
    void shouldAcceptFormWithEmptyDescription() {

        EditCourseForm form = new EditCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("");
        form.setStatus(CourseStatus.ACTIVE);


        Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);


        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept valid codes with different patterns")
    void shouldAcceptValidCodesWithDifferentPatterns() {
        String[] validCodes = {"java", "spring", "react-js", "node-js", "python"};
        
        for (String code : validCodes) {
            EditCourseForm form = new EditCourseForm();
            form.setName("Test Course");
            form.setCode(code);
            form.setInstructor("Instructor");
            form.setCategory("Category");
            form.setDescription("Description");
            form.setStatus(CourseStatus.ACTIVE);
            
            Set<ConstraintViolation<EditCourseForm>> violations = validator.validate(form);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")))
                    .as("Code '%s' should be valid", code)
                    .isFalse();
        }
    }
}
