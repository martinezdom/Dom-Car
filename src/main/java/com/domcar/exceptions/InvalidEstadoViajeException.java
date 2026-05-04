package com.domcar.exceptions;

public class InvalidEstadoViajeException extends RuntimeException {
    public InvalidEstadoViajeException() {
        super("El estado de viaje asignado es incorrecto");
    }
}
