package br.com.alura.projeto.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "Category")
public class Category {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @Setter
    @Getter
    @NotBlank(message = "Código é obrigatório")
    private String code;

    @Setter
    @Getter
    @NotBlank(message = "Cor é obrigatória")
    private String color;

    @Setter
    @Getter
    @NotNull(message = "Ordem é obrigatória")
    @Min(value = 1, message = "Ordem deve ser pelo menos 1")
    @Column(name = "`order`")
    private int order;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Deprecated
    public Category() {}

    public Category(String name, String code, String color, int order) {
        this.name = name;
        this.code = code;
        this.color = color;
        this.order = order;
    }

}
