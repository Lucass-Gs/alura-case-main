package br.com.alura.projeto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewRegistrationDTO {

    @NotBlank
    @NotNull
    private String courseCode;

    @NotBlank
    @NotNull
    @Email
    private String studentEmail;

}
