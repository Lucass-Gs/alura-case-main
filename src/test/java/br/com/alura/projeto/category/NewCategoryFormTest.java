package br.com.alura.projeto.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NewCategoryFormTest {

    @Test
    @DisplayName("should create form with valid data")
    void shouldCreateFormWithValidData() {
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(1);

        assertThat(form.getName()).isEqualTo("Programação");
        assertThat(form.getCode()).isEqualTo("prog");
        assertThat(form.getColor()).isEqualTo("#00C86F");
        assertThat(form.getOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("should reject form with blank name")
    void shouldRejectFormWithBlankName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

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
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank color")
    void shouldRejectFormWithBlankColor() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("color") && 
                              v.getMessage().contains("Cor é obrigatória")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too short")
    void shouldRejectFormWithCodeTooShort() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("ab");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 10 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too long")
    void shouldRejectFormWithCodeTooLong() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("very-very-long-code");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 10 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code containing numbers")
    void shouldRejectFormWithCodeContainingNumbers() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog123");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code containing spaces")
    void shouldRejectFormWithCodeContainingSpaces() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog code");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code containing special characters")
    void shouldRejectFormWithCodeContainingSpecialCharacters() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog@#$");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve conter apenas letras e hífens")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with order less than 1")
    void shouldRejectFormWithOrderLessThan1() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(0);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("order") && 
                              v.getMessage().contains("Ordem deve ser pelo menos 1")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with negative order")
    void shouldRejectFormWithNegativeOrder() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(-1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

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
        
        String[] validCodes = {"prog", "front-end", "mobile", "devops"};
        
        for (String code : validCodes) {
            NewCategoryForm form = new NewCategoryForm();
            form.setName("Test Category");
            form.setCode(code);
            form.setColor("#00C86F");
            form.setOrder(1);
            
            Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);
            
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
            NewCategoryForm form = new NewCategoryForm();
            form.setName("Test Category");
            form.setCode("test");
            form.setColor("#00C86F");
            form.setOrder(order);
            
            Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);
            
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
            NewCategoryForm form = new NewCategoryForm();
            form.setName("Test Category");
            form.setCode("test");
            form.setColor(color);
            form.setOrder(1);
            
            Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("color")))
                    .as("Color '%s' should be valid", color)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("should create form with default constructor")
    void shouldCreateFormWithDefaultConstructor() {

        NewCategoryForm form = new NewCategoryForm();


        assertThat(form.getName()).isNull();
        assertThat(form.getCode()).isNull();
        assertThat(form.getColor()).isNull();
        assertThat(form.getOrder()).isEqualTo(0);
    }

    @Test
    @DisplayName("should create form with all fields set")
    void shouldCreateFormWithAllFieldsSet() {

        NewCategoryForm form = new NewCategoryForm();
        form.setName("Frontend");
        form.setCode("frontend");
        form.setColor("#6BD1FF");
        form.setOrder(2);


        assertThat(form.getName()).isEqualTo("Frontend");
        assertThat(form.getCode()).isEqualTo("frontend");
        assertThat(form.getColor()).isEqualTo("#6BD1FF");
        assertThat(form.getOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("should handle form with minimum valid data")
    void shouldHandleFormWithMinimumValidData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("A");
        form.setCode("abcd");
        form.setColor("#000000");
        form.setOrder(1);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should handle form with maximum valid data")
    void shouldHandleFormWithMaximumValidData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        NewCategoryForm form = new NewCategoryForm();
        form.setName("A".repeat(100));
        form.setCode("abcdefghij");
        form.setColor("#FFFFFF");
        form.setOrder(Integer.MAX_VALUE);

        Set<ConstraintViolation<NewCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }
}
