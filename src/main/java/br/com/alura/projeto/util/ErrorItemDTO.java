package br.com.alura.projeto.util;

import org.springframework.util.Assert;
import org.springframework.validation.FieldError;

public record ErrorItemDTO(String field, String message) {

    public ErrorItemDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public ErrorItemDTO {
        Assert.notNull(field, "Descrição do campo não deve ser nula");
        Assert.isTrue(!field.isEmpty(), "Descrição do campo não deve estar vazia");

        Assert.notNull(message, "Descrição da mensagem não deve ser nula");
        Assert.isTrue(!message.isEmpty(), "Descrição da mensagem não deve estar vazia");

    }
}
