package encriptacion;

import lombok.extern.log4j.Log4j2;
import model.Usuario;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Log4j2
public class Claves {


    public PublicKey getClavePublicaRegistro(String clave64) {
        PublicKey clavePublica = null;
        try {
            KeyFactory keyFactoryRSA = null; // Hace uso del provider BC
            keyFactoryRSA = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(Base64.getUrlDecoder().decode(clave64));
            clavePublica = keyFactoryRSA.generatePublic(clavePublicaSpec);
        } catch (Exception e) {

        }
        return clavePublica;
    }

    public PrivateKey getClavePrivadaServidor(HttpServletRequest request) {
        PrivateKey clavePrivadaServidor = null;
        InputStream is = request.getServletContext().getResourceAsStream("/WEB-INF/servidor.pfx");
        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(is, "".toCharArray());
            KeyStore.PasswordProtection pt = new KeyStore.PasswordProtection("".toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ksLoad.getEntry("servidor" + "privada", pt);

            clavePrivadaServidor = privateKeyEntry.getPrivateKey();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return clavePrivadaServidor;
    }

    public PublicKey getClavePublicaServidor(HttpServletRequest request) {
        PublicKey clavePublicaServidor = null;
        InputStream is = request.getServletContext().getResourceAsStream("/WEB-INF/servidor.pfx");

        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(is, "".toCharArray());
            X509Certificate certLoad = (X509Certificate) ksLoad.getCertificate("servidor" + "publica");
            clavePublicaServidor = certLoad.getPublicKey();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return clavePublicaServidor;
    }

    public PublicKey getClavePublicaCert(Usuario usuario){
        PublicKey clavePublicaCertificado = null;

        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(new FileInputStream(usuario.getRutaCert()), "".toCharArray());
            X509Certificate certLoad = (X509Certificate) ksLoad.getCertificate("publica");
            clavePublicaCertificado = certLoad.getPublicKey();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return clavePublicaCertificado;
    }
}
