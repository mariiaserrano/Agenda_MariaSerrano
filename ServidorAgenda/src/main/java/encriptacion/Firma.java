package encriptacion;

import lombok.extern.log4j.Log4j2;
import model.Usuario;
import model.UsuarioLogin;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
@Log4j2
public class Firma {

    private Claves cl = new Claves();

    public boolean comprobarFirma(PublicKey claveUsuario, UsuarioLogin usuario){
        boolean comprueba = false;
        try{
            Signature sing = Signature.getInstance("SHA256WithRSA");
            sing.initVerify(claveUsuario);
            sing.update(usuario.getNombre().getBytes());
            comprueba= sing.verify(Base64.getUrlDecoder().decode(usuario.getFirma()));
        }
        catch (Exception e ){
            log.error(e.getMessage());
        }
        return comprueba;
    }
}
