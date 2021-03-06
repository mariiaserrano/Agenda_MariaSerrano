package Errores;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDate;

@Provider
public class ValidatorExceptionMapper implements ExceptionMapper<ValidatorException> {
    @Override
    public Response toResponse(ValidatorException e) {
        ApiError apiError = new ApiError(e.getMessage(), LocalDate.now());
        return Response.status(Response.Status.BAD_REQUEST).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
