package br.com.alura.projeto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserListItemDTO implements Serializable {

    private String name;
    private String email;
    private Role role;

    public UserListItemDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
