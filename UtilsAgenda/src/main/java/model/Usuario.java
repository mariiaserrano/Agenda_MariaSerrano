package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    private String usuario;
    private String contraseña;
    private String rutaCert;

    public Usuario(String usuario,String rutaCert){
        this.usuario=usuario;
        this.rutaCert = rutaCert;
    }
}
