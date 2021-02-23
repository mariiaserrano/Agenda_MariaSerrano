package rest;

import encriptacion.Certificados;
import encriptacion.Claves;
import encriptacion.Firma;
import model.Usuario;
import model.UsuarioLogin;
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
    private Claves cl = new Claves();
    private Firma fr = new Firma();

    @Context
    HttpServletRequest request;


    @POST
    public UsuarioRegistro addUsuario(UsuarioRegistro usuario) {
            return sl.addUsuario(usuario,request).get();
    }

    @GET
    public Usuario login (@QueryParam("nombre") String nombre, @QueryParam("firma") String firma){
        Usuario usuario = new Usuario(nombre,"","");
//        String ruta = sl.login(usuario).get().getRutaCert();
        UsuarioLogin usuarioLogin = new UsuarioLogin(nombre,firma,"");
//        usuario.setRutaCert(ruta);
//        cl.getClavePublicaCert(usuario);
//        System.out.println(cl.getClavePublicaCert(usuario));
        fr.comprobarFirma(cl.getClavePublicaCert(usuario),usuarioLogin);
        System.out.println(fr.comprobarFirma(cl.getClavePublicaCert(usuario),usuarioLogin));

        return sl.login(usuario).get();
    }


}

