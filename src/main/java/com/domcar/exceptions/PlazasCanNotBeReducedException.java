package com.domcar.exceptions;

public class PlazasCanNotBeReducedException extends RuntimeException {

    public PlazasCanNotBeReducedException() {
        super("Las plazas de un vehículo no pueden ser reducidas");
    }

}
