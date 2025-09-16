package br.com.alura.projeto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class EditCategoryForm {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Código é obrigatório")
    @Length(min = 4, max = 10, message = "Código deve ter entre 4 e 10 caracteres")
    private String code;

    @Min(value = 1, message = "Ordem deve ser pelo menos 1")
    private int order;

    @NotBlank(message = "Cor é obrigatória")
    private String color;

    public EditCategoryForm() {}

    public EditCategoryForm(Category category) {
        this.name = category.getName();
        this.code = category.getCode();
        this.color = category.getColor();
        this.order = category.getOrder();
    }

    public void updateCategory(Category category) {
        category.setName(this.name);
        category.setCode(this.code);
        category.setColor(this.color);
        category.setOrder(this.order);
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
