package br.com.alura.projeto.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    @DisplayName("should create category with valid data")
    void shouldCreateCategoryWithValidData() {
        String name = "Programação";
        String code = "prog";
        String color = "#FF6B6B";
        int order = 1;

        Category category = new Category(name, code, color, order);

        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getCode()).isEqualTo(code);
        assertThat(category.getColor()).isEqualTo(color);
        assertThat(category.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("should reject category with blank name")
    void shouldRejectCategoryWithBlankName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("", "prog", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with blank code")
    void shouldRejectCategoryWithBlankCode() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with blank color")
    void shouldRejectCategoryWithBlankColor() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "prog", "", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("color") && 
                              v.getMessage().contains("Cor é obrigatória")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with code too short")
    void shouldRejectCategoryWithCodeTooShort() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "ab", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with code too long")
    void shouldRejectCategoryWithCodeTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "very-very-very-long-code", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 15 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with code containing numbers")
    void shouldRejectCategoryWithCodeContainingNumbers() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "prog123", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with code containing spaces")
    void shouldRejectCategoryWithCodeContainingSpaces() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "prog code", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with code containing special characters")
    void shouldRejectCategoryWithCodeContainingSpecialCharacters() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "prog@#$", "#FF6B6B", 1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with order less than 1")
    void shouldRejectCategoryWithOrderLessThan1() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "prog", "#FF6B6B", 0);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("order") && 
                              v.getMessage().contains("Ordem deve ser pelo menos 1")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject category with negative order")
    void shouldRejectCategoryWithNegativeOrder() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Category category = new Category("Programação", "prog", "#FF6B6B", -1);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("order") && 
                              v.getMessage().contains("Ordem deve ser pelo menos 1")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept valid codes with different patterns")
    void shouldAcceptValidCodesWithDifferentPatterns() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String[] validCodes = {"prog", "front-end", "data-science", "mobile", "devops"};
        
        for (String code : validCodes) {
            Category category = new Category("Test Category", code, "#FF6B6B", 1);
            Set<ConstraintViolation<Category>> violations = validator.validate(category);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")))
                    .as("Code '%s' should be valid", code)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("should accept valid orders")
    void shouldAcceptValidOrders() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        int[] validOrders = {1, 5, 10, 100, 1000};
        
        for (int order : validOrders) {
            Category category = new Category("Test Category", "test", "#FF6B6B", order);
            Set<ConstraintViolation<Category>> violations = validator.validate(category);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("order")))
                    .as("Order '%d' should be valid", order)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("should accept valid color formats")
    void shouldAcceptValidColorFormats() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        String[] validColors = {"#FF6B6B", "#00C86F", "#6BD1FF", "#9CD33B", "#FF8C00"};
        
        for (String color : validColors) {
            Category category = new Category("Test Category", "test", color, 1);
            Set<ConstraintViolation<Category>> violations = validator.validate(category);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("color")))
                    .as("Color '%s' should be valid", color)
                    .isFalse();
        }
    }
}

