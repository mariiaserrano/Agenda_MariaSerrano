package encriptacion;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.Usuario;
import model.UsuarioLogin;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import retrofit.ApiError;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j2
public class Claves {

    public X509Certificate generarCertificado(PublicKey clavepublica, Usuario usuario) {
        X509Certificate certificate = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair claveRSA = keyGen.generateKeyPair();
            //genera certificado
            X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
            cert1.setSerialNumber(BigInteger.valueOf(1));
            //usuario
            cert1.setSubjectDN(new X509Principal("CN=" + usuario.getUsuario()));
            cert1.setIssuerDN(new X509Principal("CN=" + usuario.getUsuario()));
            cert1.setPublicKey(clavepublica);
            cert1.setNotBefore(
                    Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
            cert1.setNotAfter(new Date());
            cert1.setSignatureAlgorithm("SHA1WithRSAEncryption");
            //firmamos
            PrivateKey signingKey = claveRSA.getPrivate();
            X509Certificate cert = cert1.generateX509Certificate(signingKey);
            certificate = cert;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

        }
        return certificate;

    }

    public Either<ApiError, KeyPair> generarClaves(Usuario usuario) {
        Either<ApiError, KeyPair> resultado = null;

        KeyPairGenerator keyGen = null;
        BouncyCastleProviderSingleton.getInstance();
        X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
        KeyStore ks = null;

        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair clavesRSA = keyGen.generateKeyPair();

            ks = KeyStore.getInstance("PKCS12");

            char[] password = usuario.getContraseña().toCharArray();

            ks.load(null, null);
           // no guardamos la clave publica ks.setCertificateEntry(usuario.getUsuario() + "publica", generarCertificado(clavesRSA.getPublic(), usuario));
            ks.setKeyEntry(usuario.getUsuario() + "privada", clavesRSA.getPrivate(), password, new Certificate[]{generarCertificado(clavesRSA.getPublic(), usuario)});
            FileOutputStream fos = new FileOutputStream("archivos/" + usuario.getUsuario() + ".pfx");
            ks.store(fos, "".toCharArray());
            fos.close();
            resultado = Either.right(clavesRSA);
        } catch (Exception e) {
            resultado = Either.left(ApiError.builder().build());

        }
        return resultado;
    }

    public PrivateKey getClavePrivada(UsuarioLogin usuario) {
        PrivateKey clavePrivadaRecuperada = null;
        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(new FileInputStream("archivos/" + usuario.getNombre() + ".pfx"), "".toCharArray());
            KeyStore.PasswordProtection pt = new KeyStore.PasswordProtection(usuario.getPass().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ksLoad.getEntry(usuario.getNombre() + "privada", pt);

            clavePrivadaRecuperada = privateKeyEntry.getPrivateKey();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return clavePrivadaRecuperada;

    }

    public PublicKey getClavePublica(UsuarioLogin usuario) {
        Either<String, PublicKey> resultado = null;
        PublicKey clavePublicaRecuperada = null;
        try {
            KeyStore ksLoad = KeyStore.getInstance("PKCS12");
            ksLoad.load(new FileInputStream("archivos/" + usuario.getNombre() + ".pfx"), "".toCharArray());
            X509Certificate certLoad = (X509Certificate) ksLoad.getCertificate(usuario.getNombre() + "publica");
            clavePublicaRecuperada = certLoad.getPublicKey();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return clavePublicaRecuperada;

    }
}
