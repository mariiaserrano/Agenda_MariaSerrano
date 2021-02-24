package rest;

import io.vavr.control.Either;
import model.Cita;
import model.UsuarioRegistro;
import servicios.ServiciosCitas;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestCitas {

    private ServiciosCitas sc = new ServiciosCitas();
    @POST
    public Either<String, Cita> addCita(Cita cita) {
        return sc.addCita(cita);
    }

}
