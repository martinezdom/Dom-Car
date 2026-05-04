package com.domcar.exceptions;

public class ViajeNotFoundException extends Exception {

    public ViajeNotFoundException(int codViaje) {
         super("El viaje con " + codViaje + " no ha sido encontrado");
    }

    public ViajeNotFoundException(String msg) {
        super(msg);
    }

}
