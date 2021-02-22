package encriptacion;

import Errores.ApiError;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import model.Usuario;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j2
public class CreaClavesServidor {
    public static void main(String args[]) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, SignatureException, InvalidKeyException {
        Either<ApiError, KeyPair> resultado = null;

        KeyPairGenerator keyGen = null;
         BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
        Security.addProvider(bouncyCastleProvider);
      //  X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
        KeyStore ks = null;


            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair clavesRSA = keyGen.generateKeyPair();

            ks = KeyStore.getInstance("PKCS12");

        //genera certificado
        X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
        cert1.setSerialNumber(BigInteger.valueOf(1));
        //usuario
        cert1.setSubjectDN(new X509Principal("CN=servidor"));
        cert1.setIssuerDN(new X509Principal("CN=servidor"));
        cert1.setPublicKey(clavesRSA.getPublic());
        System.out.println("PUBLICA ");
        System.out.println(clavesRSA.getPublic());
        cert1.setNotBefore(
                Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
        cert1.setNotAfter(new Date());
        cert1.setSignatureAlgorithm("SHA1WithRSAEncryption");
        //firmamos
        PrivateKey signingKey = clavesRSA.getPrivate();
        System.out.println("PRIVADA");
        System.out.println(clavesRSA.getPrivate());
        X509Certificate cert = cert1.generateX509Certificate(signingKey);





            char[] password = "".toCharArray();

            ks.load(null, null);
            ks.setCertificateEntry("servidor"+ "publica", cert);
            ks.setKeyEntry("servidor" + "privada", clavesRSA.getPrivate(), password, new X509Certificate[]{cert});
            FileOutputStream fos = new FileOutputStream("ServidorAgenda/src/main/webapp/WEB-INF/claves/" + "servidor.pfx");
            ks.store(fos, "".toCharArray());
            fos.close();

    }

    public static X509Certificate generarCertificado(PublicKey clavepublica) {
        X509Certificate certificate = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair claveRSA = keyGen.generateKeyPair();
            //genera certificado
            X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
            cert1.setSerialNumber(BigInteger.valueOf(1));
            //usuario
            cert1.setSubjectDN(new X509Principal("CN=servidor"));
            cert1.setIssuerDN(new X509Principal("CN=servidor"));
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
}
