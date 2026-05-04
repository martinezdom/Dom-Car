package com.domcar.exceptions;

public class InvalidNumberOfPlacesException extends RuntimeException {

    public InvalidNumberOfPlacesException() {
        super("El número máximo de plazas por coche es 6");
    }
}
