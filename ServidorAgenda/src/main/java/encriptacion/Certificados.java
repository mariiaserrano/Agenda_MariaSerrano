package encriptacion;

import lombok.extern.log4j.Log4j2;
import model.Usuario;
import model.UsuarioRegistro;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import servicios.ServiciosUsuarios;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j2
public class Certificados {

   private Claves cl = new Claves();
  // private ServiciosUsuarios sl = new ServiciosUsuarios();

    public X509Certificate certificadoRegistro(UsuarioRegistro usuario, HttpServletRequest request) {
        X509Certificate cert = null;
        ServiciosUsuarios sl = new ServiciosUsuarios();

        String webInfPath = request.getServletContext().getRealPath("/WEB-INF/");
        try {
            PrivateKey clavePrivadaServidor = cl.getClavePrivadaServidor(request);
            PublicKey clavePublicaCliente = cl.getClavePublicaRegistro(usuario.getClavePublica());
            BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
            Security.addProvider(bouncyCastleProvider);
            X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
            cert1.setSerialNumber(BigInteger.valueOf(1));
            cert1.setSubjectDN(new X509Principal("CN=" + usuario.getUsuario().getUsuario()));
            cert1.setIssuerDN(new X509Principal("CN=servidor"));
            cert1.setPublicKey(clavePublicaCliente);
            cert1.setNotBefore(
                    Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
            cert1.setNotAfter(new Date());
            cert1.setSignatureAlgorithm("SHA1WithRSAEncryption");
            PrivateKey signingKey = clavePrivadaServidor;

            cert = cert1.generateX509Certificate(signingKey);
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(null, null);
            ks.setCertificateEntry("publica",cert);
            FileOutputStream fos = new FileOutputStream(webInfPath+"/"+usuario.getUsuario().getUsuario()+"Certificado.pfx");
            ks.store(fos,"".toCharArray());
            fos.close();



            usuario.getUsuario().setRutaCert(webInfPath+"/"+usuario.getUsuario().getUsuario()+"Certificado.pfx");
            sl.updateRutaCert(usuario);
        } catch (Exception e) {
          log.error(e.getMessage(),e);
        }
        return cert;

    }
}
