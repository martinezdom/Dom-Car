package com.domcar.exceptions;

public class ReservaNotFoundException extends Exception {

    public ReservaNotFoundException(String codReserva) {
        super("La reserva con código " + codReserva + " no ha sido encontrada");
    }

}
