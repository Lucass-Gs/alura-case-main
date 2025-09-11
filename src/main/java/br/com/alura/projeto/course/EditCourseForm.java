package br.com.alura.projeto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EditCourseForm {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(min = 4, max = 10, message = "Code must be between 4 and 10 characters")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Code must contain only letters and hyphens, no spaces, numbers or special characters")
    private String code;

    @NotBlank(message = "Instructor is required")
    @Size(max = 100, message = "Instructor name must not exceed 100 characters")
    private String instructor;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private CourseStatus status;

    public EditCourseForm() {}

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
