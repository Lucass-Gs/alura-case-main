package br.com.alura.projeto.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome não pode exceder 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 4, max = 15, message = "Código deve ter entre 4 e 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Código deve conter apenas letras e hífens, sem espaços, números ou caracteres especiais")
    @Column(name = "code", nullable = false, unique = true, length = 10)
    @EqualsAndHashCode.Include
    private String code;

    @NotBlank(message = "Instrutor é obrigatório")
    @Size(max = 100, message = "Nome do instrutor não pode exceder 100 caracteres")
    @Column(name = "instructor", nullable = false, length = 100)
    private String instructor;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria não pode exceder 50 caracteres")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseStatus status = CourseStatus.ACTIVE;

    @Column(name = "inactivation_date")
    private LocalDateTime inactivationDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Course(String name, String code, String instructor, String category, String description) {
        this.name = name;
        this.code = code;
        this.instructor = instructor;
        this.category = category;
        this.description = description;
    }

    public void inactivate() {
        this.status = CourseStatus.INACTIVE;
        this.inactivationDate = LocalDateTime.now();
    }

    public void activate() {
        this.status = CourseStatus.ACTIVE;
        this.inactivationDate = null;
    }

    public boolean isActive() {
        return this.status == CourseStatus.ACTIVE;
    }
}