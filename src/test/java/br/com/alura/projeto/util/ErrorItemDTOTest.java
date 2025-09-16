package br.com.alura.projeto.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorItemDTOTest {

    @Test
    @DisplayName("should create ErrorItemDTO with valid field and message")
    void shouldCreateErrorItemDTOWithValidFieldAndMessage() {
        String field = "email";
        String message = "Email is required";

        ErrorItemDTO errorItem = new ErrorItemDTO(field, message);

        assertThat(errorItem.field()).isEqualTo(field);
        assertThat(errorItem.message()).isEqualTo(message);
    }

    @Test
    @DisplayName("should create ErrorItemDTO from FieldError")
    void shouldCreateErrorItemDTOFromFieldError() {
        FieldError fieldError = new FieldError("user", "password", "123", false, null, null, "Senha deve ter pelo menos 8 caracteres");

        ErrorItemDTO errorItem = new ErrorItemDTO(fieldError);

        assertThat(errorItem.field()).isEqualTo("password");
        assertThat(errorItem.message()).isEqualTo("Senha deve ter pelo menos 8 caracteres");
    }

    @Test
    @DisplayName("should throw exception when field is null")
    void shouldThrowExceptionWhenFieldIsNull() {

        assertThatThrownBy(() -> new ErrorItemDTO(null, "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Descrição do campo não deve ser nula");
    }

    @Test
    @DisplayName("should throw exception when field is empty")
    void shouldThrowExceptionWhenFieldIsEmpty() {

        assertThatThrownBy(() -> new ErrorItemDTO("", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Descrição do campo não deve estar vazia");
    }

    @Test
    @DisplayName("should throw exception when message is null")
    void shouldThrowExceptionWhenMessageIsNull() {

        assertThatThrownBy(() -> new ErrorItemDTO("field", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Descrição da mensagem não deve ser nula");
    }

    @Test
    @DisplayName("should throw exception when message is empty")
    void shouldThrowExceptionWhenMessageIsEmpty() {

        assertThatThrownBy(() -> new ErrorItemDTO("field", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Descrição da mensagem não deve estar vazia");
    }

    @ParameterizedTest
    @ValueSource(strings = {"name", "email", "password", "code", "description"})
    @DisplayName("should accept valid field names")
    void shouldAcceptValidFieldNames(String fieldName) {

        String message = "Validation error message";


        ErrorItemDTO errorItem = new ErrorItemDTO(fieldName, message);


        assertThat(errorItem.field()).isEqualTo(fieldName);
        assertThat(errorItem.message()).isEqualTo(message);
    }

    @Test
    @DisplayName("should handle long field names and messages")
    void shouldHandleLongFieldNamesAndMessages() {

        String longFieldName = "veryLongFieldNameThatMightBeUsedInComplexForms";
        String longMessage = "This is a very long validation error message that might be used to provide detailed information about what went wrong with the field validation";


        ErrorItemDTO errorItem = new ErrorItemDTO(longFieldName, longMessage);


        assertThat(errorItem.field()).isEqualTo(longFieldName);
        assertThat(errorItem.message()).isEqualTo(longMessage);
    }

    @Test
    @DisplayName("should handle special characters in field and message")
    void shouldHandleSpecialCharactersInFieldAndMessage() {

        String fieldWithSpecialChars = "field_with_underscores";
        String messageWithSpecialChars = "Error message with special chars: !@#$%^&*()";


        ErrorItemDTO errorItem = new ErrorItemDTO(fieldWithSpecialChars, messageWithSpecialChars);


        assertThat(errorItem.field()).isEqualTo(fieldWithSpecialChars);
        assertThat(errorItem.message()).isEqualTo(messageWithSpecialChars);
    }
}
