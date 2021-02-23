package encriptacion;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.UsuarioLogin;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

@Log4j2
public class Firma {
    private Claves cl = new Claves();

    public Either<String, UsuarioLogin> firmaNombre(UsuarioLogin usuario) {
        Either<String, UsuarioLogin> resultado = null;
        PrivateKey clavePrivadaUsuario = cl.getClavePrivada(usuario);
        Signature sing = null;
        try {
            sing = Signature.getInstance("SHA256WithRSA");
            sing.initSign(clavePrivadaUsuario);
            sing.update(usuario.getNombre().getBytes());
            usuario.setFirma(Base64.getUrlEncoder().encodeToString(sing.sign()));
            resultado = Either.right(usuario);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultado;

    }
}
