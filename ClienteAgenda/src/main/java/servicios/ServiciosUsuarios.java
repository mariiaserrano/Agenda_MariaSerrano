package servicios;

import dao.DaoUsuarios;
import encriptacion.Claves;
import io.vavr.control.Either;
import model.Usuario;
import model.UsuarioRegistro;
import retrofit.ApiError;

import java.security.PublicKey;

public class ServiciosUsuarios {
    private DaoUsuarios dao = new DaoUsuarios();
    private Claves cl = new Claves();

    //    public Either<ApiError, Usuario> addUsuario(Usuario usu) {
//
//        return dao.addUsuario(usu);
//    }
//    public Either<ApiError, String> addUsuario(Usuario usu) {
//
//        return dao.addUsuario(usu);
//    }
    public Either<ApiError, UsuarioRegistro> addUsuario(Usuario usu) {

        return dao.addUsuario(usu);
    }
}
