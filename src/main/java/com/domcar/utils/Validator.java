package com.domcar.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    private static final String EXP_REG_RUTA = "^[A-Za-z]+(-[A-Za-z]+)+$";
    private static final String EXP_REG_PROPIETARIO = "^[A-Z][a-z]+\\s+[A-Z][a-z]+$";

    private static boolean esNoVacioONulo(String param) {
        return param != null && !param.isEmpty();
    }

    public static boolean isValidRuta(String ruta) {
        return esNoVacioONulo(ruta) && ruta.matches(EXP_REG_RUTA);
    }

    public static boolean isValidPropietario(String propietario) {
        return esNoVacioONulo(propietario) && propietario.matches(EXP_REG_PROPIETARIO);
    }

    public static boolean isValidPrecio(float precio) {
        return precio > 0;
    }

    public static boolean isValidDuracion(int duracion) {
        return duracion > 0;
    }

    public static boolean isValidPlazasOfertadas(int plazas) {
        return plazas > 0 && plazas <= 6;
    }
    
    public static boolean isBeforeDateTime(LocalDateTime fecha) {
    	LocalDateTime hoy = LocalDateTime.now();
    	return fecha.isAfter(hoy);
    }

    public static boolean isValidDateTime(String dateTime) {
        if (!esNoVacioONulo(dateTime)) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            formatter.parse(dateTime);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidDate(String date) {
        if (!esNoVacioONulo(date)) {
            return false;
        }
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidTime(String time) {
        if (!esNoVacioONulo(time)) {
            return false;
        }
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
