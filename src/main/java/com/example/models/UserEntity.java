package com.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data //genera getter y setter
@AllArgsConstructor  //genera contructor
@NoArgsConstructor  //genera contructor sin parametros
@Builder //implementa el patron de diseño builder para construir objetos de esta clase
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Size(max = 80)
    private String email;

    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)  //se ocupa el fect eager para que cuando se consulte el usuario se traiga todos los roles asociados al usuario , target entity es con cual entidad se va establecer la relacio
    @JoinTable(name = "use_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id")) //generamos la tabal intermedia entre dos tablas y su clave foranea de roles y de user
    private Set<RoleEntity> roles; //ocupamos set ya que set no nos permite tener elementos duplicados
}
