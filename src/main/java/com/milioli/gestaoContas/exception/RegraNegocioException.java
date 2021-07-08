package com.milioli.gestaoContas.exception;

import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RegraNegocioException extends RuntimeException {

    static final Logger LOGGER = Logger.getLogger(RegraNegocioException.class.getName());

    public RegraNegocioException(String message) {
        super(message);
    }

    public static String extractMessageFromException(Exception e) {
        LOGGER.log(Level.WARNING, e.getMessage());
        if (EntityNotFoundException.class.equals(e.getClass()) || DataIntegrityViolationException.class.equals(e.getClass())) {
            return "Registro não encontrado.";
        }
        if (ConstraintViolationException.class.equals(e.getClass())) {
            return ((ConstraintViolationException) e).getConstraintViolations().stream()
                    .map(c -> c.getConstraintDescriptor().getMessageTemplate())
                    .collect(Collectors.joining(". "));
        }
        if (NullPointerException.class.equals(e.getClass())) {
            return "Um parâmetro obrigatório não foi informado.";
        }
        return e.getMessage();
    }

}
