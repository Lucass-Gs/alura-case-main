package br.com.alura.projeto.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EditCategoryFormTest {

    private Validator validator;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        testCategory = new Category("Programação", "prog", "#00C86F", 1);
    }

    @Test
    @DisplayName("should create form from category with all fields")
    void shouldCreateFormFromCategoryWithAllFields() {
        EditCategoryForm form = new EditCategoryForm(testCategory);


        assertThat(form.getName()).isEqualTo("Programação");
        assertThat(form.getCode()).isEqualTo("prog");
        assertThat(form.getColor()).isEqualTo("#00C86F");
        assertThat(form.getOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("should update category with valid data")
    void shouldUpdateCategoryWithValidData() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação Avançada");
        form.setCode("prog-advanced");
        form.setColor("#FF6B6B");
        form.setOrder(2);

        form.updateCategory(testCategory);

        assertThat(testCategory.getName()).isEqualTo("Programação Avançada");
        assertThat(testCategory.getCode()).isEqualTo("prog-advanced");
        assertThat(testCategory.getColor()).isEqualTo("#FF6B6B");
        assertThat(testCategory.getOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("should reject form with blank name")
    void shouldRejectFormWithBlankName() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && 
                              v.getMessage().contains("Nome é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank code")
    void shouldRejectFormWithBlankCode() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código é obrigatório")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with blank color")
    void shouldRejectFormWithBlankColor() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("color") && 
                              v.getMessage().contains("Cor é obrigatória")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too short")
    void shouldRejectFormWithCodeTooShort() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("ab");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 10 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with code too long")
    void shouldRejectFormWithCodeTooLong() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("very-long-code");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("code") && 
                              v.getMessage().contains("Código deve ter entre 4 e 10 caracteres")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept form with code containing numbers (no pattern validation)")
    void shouldAcceptFormWithCodeContainingNumbers() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("prog123");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept form with code containing spaces (no pattern validation)")
    void shouldAcceptFormWithCodeContainingSpaces() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("prog code");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should accept form with code containing special characters (no pattern validation)")
    void shouldAcceptFormWithCodeContainingSpecialCharacters() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("prog@#$");
        form.setColor("#00C86F");
        form.setOrder(1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("should reject form with order less than 1")
    void shouldRejectFormWithOrderLessThan1() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(0);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("order") && 
                              v.getMessage().contains("Ordem deve ser pelo menos 1")))
                .isTrue();
    }

    @Test
    @DisplayName("should reject form with negative order")
    void shouldRejectFormWithNegativeOrder() {
        EditCategoryForm form = new EditCategoryForm();
        form.setName("Programação");
        form.setCode("prog");
        form.setColor("#00C86F");
        form.setOrder(-1);

        Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("order") && 
                              v.getMessage().contains("Ordem deve ser pelo menos 1")))
                .isTrue();
    }

    @Test
    @DisplayName("should accept valid codes with different patterns")
    void shouldAcceptValidCodesWithDifferentPatterns() {
        String[] validCodes = {"prog", "front-end", "mobile", "devops"};
        
        for (String code : validCodes) {
            EditCategoryForm form = new EditCategoryForm();
            form.setName("Test Category");
            form.setCode(code);
            form.setColor("#00C86F");
            form.setOrder(1);
            
            Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("code")))
                    .as("Code '%s' should be valid", code)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("should accept valid orders")
    void shouldAcceptValidOrders() {
        int[] validOrders = {1, 5, 10, 100, 1000};
        
        for (int order : validOrders) {
            EditCategoryForm form = new EditCategoryForm();
            form.setName("Test Category");
            form.setCode("test");
            form.setColor("#00C86F");
            form.setOrder(order);
            
            Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("order")))
                    .as("Order '%d' should be valid", order)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("should accept valid color formats")
    void shouldAcceptValidColorFormats() {
        String[] validColors = {"#FF6B6B", "#00C86F", "#6BD1FF", "#9CD33B", "#FF8C00"};
        
        for (String color : validColors) {
            EditCategoryForm form = new EditCategoryForm();
            form.setName("Test Category");
            form.setCode("test");
            form.setColor(color);
            form.setOrder(1);
            
            Set<ConstraintViolation<EditCategoryForm>> violations = validator.validate(form);
            
            assertThat(violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("color")))
                    .as("Color '%s' should be valid", color)
                    .isFalse();
        }
    }

    @Test
    @DisplayName("should create form with default constructor")
    void shouldCreateFormWithDefaultConstructor() {
        EditCategoryForm form = new EditCategoryForm();


        assertThat(form.getName()).isNull();
        assertThat(form.getCode()).isNull();
        assertThat(form.getColor()).isNull();
        assertThat(form.getOrder()).isEqualTo(0);
    }

    @Test
    @DisplayName("should update category with partial data")
    void shouldUpdateCategoryWithPartialData() {

        EditCategoryForm form = new EditCategoryForm();
        form.setName("Updated Name");
        form.setCode("updated-code");
        form.setColor(null);
        form.setOrder(0);

        form.updateCategory(testCategory);

        assertThat(testCategory.getName()).isEqualTo("Updated Name");
        assertThat(testCategory.getCode()).isEqualTo("updated-code");

        assertThat(testCategory.getColor()).isNull();
        assertThat(testCategory.getOrder()).isEqualTo(0);
    }
}
