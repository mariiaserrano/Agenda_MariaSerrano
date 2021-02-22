package servicios;

import Errores.MiException;
import dao.DaoUsuarios;
import encriptacion.Certificados;
import io.vavr.control.Either;
import model.Usuario;
import model.UsuarioRegistro;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ServiciosUsuarios {
    private DaoUsuarios dao = new DaoUsuarios();
    private Certificados cer = new Certificados();

    public Either<String, UsuarioRegistro> addUsuario(UsuarioRegistro usuario, HttpServletRequest request) {
        final StringBuilder error = new StringBuilder();
        cer.certificadoRegistro(usuario,request);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(usuario).stream().forEach(
                testDtoConstraintViolation ->
                        error.append(testDtoConstraintViolation.getMessage()));
        if (!error.toString().isEmpty()) {
            throw new MiException(error.toString());
        }
        return dao.addUsuario(usuario);
    }

    public Either<String, UsuarioRegistro> updateRutaCert(UsuarioRegistro usuario) {
        final StringBuilder error = new StringBuilder();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(usuario).stream().forEach(
                testDtoConstraintViolation ->
                        error.append(testDtoConstraintViolation.getMessage()));
        if (!error.toString().isEmpty()) {
            throw new MiException(error.toString());
        }
        return dao.updateRutaCert(usuario);
    }

    public Either<String, Usuario> login (Usuario login){
        final StringBuilder error = new StringBuilder();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(login).stream().forEach(
                testDtoConstraintViolation ->
                        error.append(testDtoConstraintViolation.getMessage()));
        if (!error.toString().isEmpty()) {
            throw new MiException(error.toString());
        }
        return dao.login(login);
    }

}