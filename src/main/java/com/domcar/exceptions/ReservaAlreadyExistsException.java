package com.domcar.exceptions;

import com.domcar.modelo.dto.Reserva;

public class ReservaAlreadyExistsException extends Exception{
	public ReservaAlreadyExistsException(Reserva reserva) {
		super("La reserva con código " + reserva.getCodigoReserva() + " ya existe");
	}

}
