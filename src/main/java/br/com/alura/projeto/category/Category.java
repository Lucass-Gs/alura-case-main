package br.com.alura.projeto.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Código é obrigatório")
    @Length(min = 4, max = 15, message = "Código deve ter entre 4 e 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+)*$", message = "Código deve conter apenas letras e hífens, sem espaços, números ou caracteres especiais")
    private String code;

    @NotBlank(message = "Cor é obrigatória")
    private String color;

    @NotNull(message = "Ordem é obrigatória")
    @Min(value = 1, message = "Ordem deve ser pelo menos 1")
    @Column(name = "`order`")
    private int order;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Category(String name, String code, String color, int order) {
        this.name = name;
        this.code = code;
        this.color = color;
        this.order = order;
    }

}
