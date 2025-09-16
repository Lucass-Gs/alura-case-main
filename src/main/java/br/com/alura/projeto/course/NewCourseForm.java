package br.com.alura.projeto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewCourseForm {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome não pode exceder 100 caracteres")
    private String name;

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 4, max = 15, message = "Código deve ter entre 4 e 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Código deve conter apenas letras e hífens, sem espaços, números ou caracteres especiais")
    private String code;

    @NotBlank(message = "Instrutor é obrigatório")
    @Size(max = 100, message = "Nome do instrutor não pode exceder 100 caracteres")
    private String instructor;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria não pode exceder 50 caracteres")
    private String category;

    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String description;

    public Course toModel() {
        return new Course(name, code, instructor, category, description);
    }
}
