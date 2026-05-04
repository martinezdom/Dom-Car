package com.domcar.exceptions;

public class MaxAttemptsException extends RuntimeException {

    public MaxAttemptsException(){
        super("Ha alcanzado el máximo número de intentos");
    }
}
