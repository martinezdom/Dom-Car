package com.domcar.modelo.dao.interfaces;

import com.domcar.exceptions.ViajeAlreadyExistsException;
import com.domcar.exceptions.ViajeNotFoundException;
import com.domcar.modelo.dto.viaje.EstadoViaje;
import com.domcar.modelo.dto.viaje.Viaje;

import java.util.Set;

public interface ViajeDAO {

    Set<Viaje> findAll();

    Set<Viaje> findAll(String city);

    Set<Viaje> findAll(EstadoViaje estadoViaje);

    Set<Viaje> findAll(Class<? extends Viaje> viajeClass);

    Viaje findById(int codViaje);

    Viaje getById(int codViaje) throws ViajeNotFoundException;

    void add(Viaje viaje) throws ViajeAlreadyExistsException;
    
    void update(Viaje viaje) throws ViajeNotFoundException;

    void remove(Viaje viaje) throws ViajeNotFoundException;

}
