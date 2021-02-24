package model;

import lombok.*;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cita {
    private String nombreEmisor;
    private String lugar;
    private String descripcion;
    private String claveSimetrica;
    private String fecha;
    private String salt;
    private String iv;
    private int iteraciones;
    private String firma;

}
