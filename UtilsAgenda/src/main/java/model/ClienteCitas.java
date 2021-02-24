package model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteCitas {
    private String clavePublica;
    private boolean iniciado;
}
