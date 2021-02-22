package model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioRegistro {
    private Usuario usuario;
    private String clavePublica;



}
