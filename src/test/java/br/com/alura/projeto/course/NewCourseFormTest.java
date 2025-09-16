package br.com.alura.projeto.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NewCourseFormTest {

    @Test
    @DisplayName("should create form with valid data")
    void shouldCreateFormWithValidData() {
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Spring Boot course");

        assertThat(form.getName()).isEqualTo("Spring Boot");
        assertThat(form.getCode()).isEqualTo("spring");
        assertThat(form.getInstructor()).isEqualTo("João Silva");
        assertThat(form.getCategory()).isEqualTo("Backend");
        assertThat(form.getDescription()).isEqualTo("Spring Boot course");
    }

    @Test
    @DisplayName("should reject form with blank name")
    void shouldRejectFormWithBlankName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank code")
    void shouldRejectFormWithBlankCode() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank instructor")
    void shouldRejectFormWithBlankInstructor() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("instructor") && 
                              v.getMessage().contains("Instrutor é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank category")
    void shouldRejectFormWithBlankCategory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("category") && 
                              v.getMessage().contains("Categoria é obrigatória")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with invalid code format")
    void shouldRejectFormWithInvalidCodeFormat() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring123");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too short")
    void shouldRejectFormWithCodeTooShort() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("ab");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too long")
    void shouldRejectFormWithCodeTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("very-very-very-long-code");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with name too long")
    void shouldRejectFormWithNameTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("A".repeat(101)); // 101 characters
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome não pode exceder 100 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with instructor name too long")
    void shouldRejectFormWithInstructorNameTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("A".repeat(101)); // 101 characters
        form.setCategory("Backend");
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("instructor") && 
                              v.getMessage().contains("Nome do instrutor não pode exceder 100 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with category too long")
    void shouldRejectFormWithCategoryTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("A".repeat(51)); // 51 characters
        form.setDescription("Description");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("category") && 
                              v.getMessage().contains("Categoria não pode exceder 50 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with description too long")
    void shouldRejectFormWithDescriptionTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("A".repeat(501)); // 501 characters

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description") && 
                              v.getMessage().contains("Descrição não pode exceder 500 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept form with null description")
    void shouldAcceptFormWithNullDescription() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription(null);

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept form with empty description")
    void shouldAcceptFormWithEmptyDescription() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCourseForm form = new NewCourseForm();
        form.setName("Spring Boot");
        form.setCode("spring");
        form.setInstructor("João Silva");
        form.setCategory("Backend");
        form.setDescription("");

        Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept valid codes with different patterns")
    void shouldAcceptValidCodesWithDifferentPatterns() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String[] validCodes = {"java", "spring-boot", "react-js", "node-js", "python-django"};
        
        for (String code : validCodes) {
            NewCourseForm form = new NewCourseForm();
            form.setName("Test Course");
            form.setCode(code);
            form.setInstructor("Instructor");
            form.setCategory("Category");
            form.setDescription("Description");
            
            Set<ConstraintViolation<NewCourseForm>> violations = validator.validate(form);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")))
                    .as("Code '%s' should be valid", code)
                    .isFalse();
        }
    }
}

