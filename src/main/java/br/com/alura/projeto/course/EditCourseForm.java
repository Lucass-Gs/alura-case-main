package br.com.alura.projeto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditCourseForm {

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

    private CourseStatus status;

    public EditCourseForm(Course course) {
        this.name = course.getName();
        this.code = course.getCode();
        this.instructor = course.getInstructor();
        this.category = course.getCategory();
        this.description = course.getDescription();
        this.status = course.getStatus();
    }

    public void updateCourse(Course course) {
        course.setName(this.name);
        course.setCode(this.code);
        course.setInstructor(this.instructor);
        course.setCategory(this.category);
        course.setDescription(this.description);

        if (this.status == CourseStatus.ACTIVE) {
            course.activate();
        } else if (this.status == CourseStatus.INACTIVE) {
            course.inactivate();
        }
    }
}
