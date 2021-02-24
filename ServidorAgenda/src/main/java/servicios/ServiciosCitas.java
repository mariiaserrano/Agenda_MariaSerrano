package servicios;

import Errores.MiException;
import dao.DaoCitas;
import io.vavr.control.Either;
import model.Cita;
import model.UsuarioRegistro;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ServiciosCitas {
    private DaoCitas dao = new DaoCitas();

    public Either<String, Cita> addCita(Cita cita) {
        final StringBuilder error = new StringBuilder();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validator.validate(cita).stream().forEach(
                testDtoConstraintViolation ->
                        error.append(testDtoConstraintViolation.getMessage()));
        if (!error.toString().isEmpty()) {
            throw new MiException(error.toString());
        }
        return dao.addCitas(cita);
    }
}
