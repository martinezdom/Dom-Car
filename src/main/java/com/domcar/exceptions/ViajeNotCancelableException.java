package com.domcar.exceptions;

public class ViajeNotCancelableException extends Exception{

    public ViajeNotCancelableException(String codViaje) {
        super("El viaje " + codViaje + " no permite ser cancelado");
    }
}
