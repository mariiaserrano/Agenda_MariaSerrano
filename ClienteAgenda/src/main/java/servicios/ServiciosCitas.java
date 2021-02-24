package servicios;

import dao.DaoCitas;
import dao.DaoUsuarios;
import encriptacion.Cifrar;
import io.vavr.control.Either;
import model.Cita;
import model.Usuario;
import model.UsuarioLogin;
import model.UsuarioRegistro;
import retrofit.ApiError;

public class ServiciosCitas {
    Cifrar cifra = new Cifrar();
    DaoCitas dao = new DaoCitas();

    public Either<ApiError, Cita> addCita(Cita cita, UsuarioLogin usu,String clavepublicaCodificada) {
        cifra.cifrarCita(cita,usu,clavepublicaCodificada);
        return dao.addCita(cita);
    }
}
