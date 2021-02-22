package rest;

import encriptacion.Certificados;
import encriptacion.Claves;
import model.Usuario;
import model.UsuarioRegistro;
import servicios.ServiciosUsuarios;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Path("usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestUsuarios {

    private ServiciosUsuarios sl = new ServiciosUsuarios();

    @Context
    HttpServletRequest request;


    @POST
    public UsuarioRegistro addUsuario(UsuarioRegistro usuario) {
            return sl.addUsuario(usuario,request).get();
    }


}

