package rest;

import encriptacion.Certificados;
import encriptacion.Claves;
import encriptacion.Firma;
import model.*;
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
    private Claves cl = new Claves();
    private Firma fr = new Firma();
    private String clavePublicaCliente = null;

    @Context
    HttpServletRequest request;


    @POST
    public UsuarioRegistro addUsuario(UsuarioRegistro usuario) {
        return sl.addUsuario(usuario, request).get();
    }

    @GET
    public ClienteCitas login(@QueryParam("nombre") String nombre, @QueryParam("firma") String firma) {
        Usuario usuario = new Usuario(nombre, "", "");
        String ruta = sl.login(usuario).get().getRutaCert();
        usuario.setRutaCert(ruta);
        UsuarioLogin usuarioLogin = new UsuarioLogin(nombre, firma, "");
        ClienteCitas citas = new ClienteCitas();
        PublicKey clavePublicaCliente = cl.getClavePublicaCert(usuario);
        String claveCifrada = Base64.getUrlEncoder().encodeToString(clavePublicaCliente.getEncoded());
        Boolean comprobado = null;

        var sesion = request.getSession();




        if (fr.comprobarFirma(cl.getClavePublicaCert(usuario), usuarioLogin)) {

            comprobado = true;
            sesion.setAttribute("usuario", nombre);

        } else {
            comprobado = false;
        }
        citas = new ClienteCitas(claveCifrada,comprobado);

        return citas;
    }


}

