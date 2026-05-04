package com.domcar.exceptions;

public class ReservaNoValidaException extends Exception {

    public ReservaNoValidaException(String motivo) {
        super("No se ha podido realizar la reserva. Motivo: " + motivo);
    }
}


