package br.com.alura.projeto.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationExceptionHandlerTest {

    private ValidationExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ValidationExceptionHandler();
    }

    @Test
    @DisplayName("should handle validation exceptions and return bad request with error list")
    void shouldHandleValidationExceptionsAndReturnBadRequestWithErrorList() {

        FieldError fieldError1 = new FieldError("object", "field1", "rejectedValue1", false, null, null, "Field 1 is required");
        FieldError fieldError2 = new FieldError("object", "field2", "rejectedValue2", false, null, null, "Field 2 is invalid");
        
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(fieldError1);
        bindingResult.addError(fieldError2);
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);


        ResponseEntity<List<ErrorItemDTO>> response = handler.handleValidationExceptions(exception);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        
        ErrorItemDTO error1 = response.getBody().get(0);
        assertThat(error1.field()).isEqualTo("field1");
        assertThat(error1.message()).isEqualTo("Field 1 is required");
        
        ErrorItemDTO error2 = response.getBody().get(1);
        assertThat(error2.field()).isEqualTo("field2");
        assertThat(error2.message()).isEqualTo("Field 2 is invalid");
    }

    @Test
    @DisplayName("should handle validation exceptions with no field errors")
    void shouldHandleValidationExceptionsWithNoFieldErrors() {

        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(new Object(), "object");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);


        ResponseEntity<List<ErrorItemDTO>> response = handler.handleValidationExceptions(exception);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("should handle validation exceptions with single field error")
    void shouldHandleValidationExceptionsWithSingleFieldError() {

        FieldError fieldError = new FieldError("user", "email", "invalid-email", false, null, null, "Email format is invalid");
        
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(new Object(), "user");
        bindingResult.addError(fieldError);
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);


        ResponseEntity<List<ErrorItemDTO>> response = handler.handleValidationExceptions(exception);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        
        ErrorItemDTO error = response.getBody().get(0);
        assertThat(error.field()).isEqualTo("email");
        assertThat(error.message()).isEqualTo("Email format is invalid");
    }

    @Test
    @DisplayName("should handle validation exceptions with multiple field errors for same field")
    void shouldHandleValidationExceptionsWithMultipleFieldErrorsForSameField() {

        FieldError fieldError1 = new FieldError("user", "password", "", false, null, null, "Password is required");
        FieldError fieldError2 = new FieldError("user", "password", "123", false, null, null, "Senha deve ter pelo menos 8 caracteres");
        
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(new Object(), "user");
        bindingResult.addError(fieldError1);
        bindingResult.addError(fieldError2);
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);


        ResponseEntity<List<ErrorItemDTO>> response = handler.handleValidationExceptions(exception);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        
        List<ErrorItemDTO> errors = response.getBody();
        assertThat(errors).extracting(ErrorItemDTO::field).containsExactly("password", "password");
        assertThat(errors).extracting(ErrorItemDTO::message)
                .containsExactly("Password is required", "Senha deve ter pelo menos 8 caracteres");
    }
}
