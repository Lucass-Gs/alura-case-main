package br.com.alura.projeto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class NewCategoryForm {

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 4, max = 10, message = "Código deve ter entre 4 e 10 caracteres")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Código deve conter apenas letras e hífens, sem espaços, números ou caracteres especiais")
    private String code;

    @Min(1)
    private int order;

    @NotBlank
    private String color;

    public Category toModel() {
        return new Category(name, code, color, order);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
