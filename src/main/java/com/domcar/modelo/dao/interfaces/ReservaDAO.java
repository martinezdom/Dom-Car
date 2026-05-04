package com.domcar.modelo.dao.interfaces;

import com.domcar.exceptions.ReservaAlreadyExistsException;
import com.domcar.exceptions.ReservaNotFoundException;
import com.domcar.modelo.dto.Reserva;
import com.domcar.modelo.dto.viaje.Viaje;

import java.util.List;
import java.util.Set;

public interface ReservaDAO {

    Set<Reserva> findAll();
    
    List<Reserva> findAllByUser(String user);

    List<Reserva> findAllByTravel(Viaje viaje);
    

    Reserva findById(String id);
    
    Reserva findByUserInTravel(String user, Viaje viaje);

    Reserva getById(String id) throws ReservaNotFoundException;
    
    int getNumPlazasReservadasEnViaje(Viaje viaje);
    
    
    void add(Reserva reserva) throws ReservaAlreadyExistsException;
    
    void update(Reserva reserva) throws ReservaNotFoundException;

    void remove(Reserva reserva) throws ReservaNotFoundException;

    List<Reserva> findAllBySearchParams(Viaje viaje, String searchParams);

}
