package com.domcar.exceptions;

public class ReservaNoModificableException extends RuntimeException {
    public ReservaNoModificableException() {
        super("El viaje no admite modificaciones de la reserva");
    }
}
