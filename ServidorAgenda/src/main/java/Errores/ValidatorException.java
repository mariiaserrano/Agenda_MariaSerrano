package Errores;

import lombok.Data;

@Data
public class ValidatorException extends RuntimeException {


    public ValidatorException(String error) {
        super(error);
    }
}
