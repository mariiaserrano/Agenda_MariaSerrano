package Errores;

import lombok.Data;

import javax.ws.rs.core.Response;

@Data
public class MiException extends RuntimeException {

  public MiException(String message) {
    super(message);
  }

  private Response.Status codigo;

}
