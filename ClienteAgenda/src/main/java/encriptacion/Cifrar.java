package encriptacion;

import fx.controllers.PrincipalController;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.Cita;
import model.Usuario;
import model.UsuarioLogin;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log4j2
public class Cifrar {
    private Claves cl = new Claves();
    private Seguridad seg = new Seguridad();

    private PrincipalController principalController;
    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }


    public Either<String, Cita> cifrarCita(Cita cita, UsuarioLogin user,String clavePublicCodificada) {
        Either<String, Cita> resultado = null;
       // String clavePublicCodificada = principalController.getClaveCliente().getClavePublica();
        PrivateKey clavePrivada;
        PublicKey clavePublicaReceptor;
        String claveSimetrica = cl.generarClaveSimetrica().get();

        byte[] iv = seg.getIv();
        byte[] salt = seg.getSalt();
        int it = 65536;

        SecretKeyFactory factory = null;
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        KeyFactory keyFactoryRSA = null;
        try {

            keyFactoryRSA = KeyFactory.getInstance("RSA");
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(claveSimetrica.toCharArray(), salt, it, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(Base64.getUrlDecoder().decode(clavePublicCodificada));

            cita.setIv(Base64.getUrlEncoder().encodeToString(iv));
            cita.setSalt(Base64.getUrlEncoder().encodeToString(salt));
            cita.setIteraciones(it);
            cita.setLugar(cifrarMensaje(cita.getLugar(), secretKey, parameterSpec));
            cita.setDescripcion(cifrarMensaje(cita.getDescripcion(),secretKey,parameterSpec));
            cita.setFecha(cifrarMensaje(cita.getFecha(),secretKey,parameterSpec));
            clavePublicaReceptor = keyFactoryRSA.generatePublic(clavePublicaSpec);

            cita.setClaveSimetrica(cifrarConSimetrica(clavePublicaReceptor, claveSimetrica));

            clavePrivada = cl.getClavePrivada(user);

            cita.setFirma(firma(clavePrivada, cita.getDescripcion()));

            cita.setNombreEmisor(principalController.getUsuario().getNombre());


            resultado = Either.right(cita);
        } catch (Exception e) {
            e.getMessage();
            resultado = Either.left(e.getMessage());
        }
        return resultado;
    }
    public String cifrarMensaje(String sinCifrar, SecretKey secretKey, GCMParameterSpec parameterSpec) {
        String cifrado = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/noPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            cifrado = Base64.getUrlEncoder().encodeToString(
                    cipher.doFinal(sinCifrar.getBytes("UTF-8")));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return cifrado;
    }

    public String cifrarConSimetrica(PublicKey clavePublica, String claveSimetrica) {
        String simetrica = null;
        try {
            Cipher cifrador = Cipher.getInstance("RSA");

            cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);  // Cifra con la clave publica
            simetrica = Base64.getUrlEncoder()
                    .encodeToString(cifrador.doFinal(claveSimetrica.getBytes()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        return simetrica;
    }

    public String firma(PrivateKey clavePrivada, String miMensaje) {
        String firmado = null;
        try {
            Signature sign = Signature.getInstance("SHA256WithRSA");
            sign.initSign(clavePrivada);
            sign.update(miMensaje.getBytes());
            firmado = Base64.getUrlEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return firmado;
    }
}
