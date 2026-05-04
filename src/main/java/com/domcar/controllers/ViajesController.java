package com.domcar.controllers;

import com.domcar.exceptions.ViajeAlreadyExistsException;
import com.domcar.exceptions.ViajeNotCancelableException;
import com.domcar.exceptions.ViajeNotFoundException;
import com.domcar.modelo.dto.viaje.Viaje;
import com.domcar.modelo.dto.Reserva;
import com.domcar.modelo.repositories.ViajesRepository;
import com.domcar.utils.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViajesController {

	@Autowired
	private ViajesRepository viajesRepository;

	@GetMapping("/")
	public String getViajesAction(@RequestParam(value = "destino", required = false) String destino, Model model)
			throws ViajeNotFoundException {
		Set<Viaje> viajes;
		if (destino != null) {
			viajes = viajesRepository.getViajesByCity(destino);
			model.addAttribute("destino", destino);
		} else {
			viajes = viajesRepository.findAll();
		}
		for (Viaje viaje : viajes) {
			int plazasReservadas = viajesRepository.getPlazasReservadasByViaje(viaje.getCodViaje());
			int plazasDisponibles = viaje.getPlazasOfertadas() - plazasReservadas;
			viaje.setPlazasDisponibles(plazasDisponibles);

			int numeroReservas = viajesRepository.getNumeroReservasByViaje(viaje);
			viaje.setNumReservas(numeroReservas);
		}
		if (viajes.isEmpty()) {
			model.addAttribute("sinResultados", "No se han encontrado viajes");
		}
		model.addAttribute("viajes", viajes);
		model.addAttribute("titulo", "Listado de viajes");
		return "viaje/listado";
	}

	@GetMapping("/viaje/add")
	public String viajeFormActionView() {
		return "viaje/viaje_form";
	}

	@PostMapping(value = "/viaje/add")
	public String insertarViaje(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
		HashMap<String, String> errores = new HashMap<>();
		String ruta = params.get("ruta");
		String plazasOfertadasString = params.get("plazasOfertadas");
		String propietario = params.get("propietario");
		String precioString = params.get("precio");
		String duracionString = params.get("duracion");
		String fechaString = params.get("fecha");
		String horaString = params.get("hora");
		String fechaYHoraString = fechaString + " " + horaString;

		int plazasOfertadas = 0;
		float precio = 0;
		int duracion = 0;
		LocalDateTime fechaYHora = null;

		try {
			plazasOfertadas = Integer.parseInt(plazasOfertadasString);
			if (!Validator.isValidPlazasOfertadas(plazasOfertadas)) {
				errores.put("Plazas", "Las plazas ofertadas deben ser un número entre 1 y 6.");
			}
		} catch (NumberFormatException e) {
			errores.put("Plazas", "Las plazas ofertadas deben ser un número válido.");
		}

		try {
			precio = Float.parseFloat(precioString);
			if (!Validator.isValidPrecio(precio)) {
				errores.put("Precio", "El precio debe ser un valor mayor a 0 y puede incluir decimales.");
			}
		} catch (NumberFormatException e) {
			errores.put("Precio", "El precio debe ser un número válido.");
		}

		try {
			duracion = Integer.parseInt(duracionString);
			if (!Validator.isValidDuracion(duracion)) {
				errores.put("Duracion", "La duración debe ser un valor mayor a 0.");
			}
		} catch (NumberFormatException e) {
			errores.put("Duracion", "La duración debe ser un número válido.");
		}

		if (!Validator.isValidDate(fechaString)) {
			errores.put("Fecha", "La fecha debe tener el formato 'yyyy-MM-dd'.");
		}
		if (!Validator.isValidTime(horaString)) {
			errores.put("Hora", "La hora debe tener el formato 'HH:mm'.");
		}
		if (!Validator.isValidDateTime(fechaYHoraString)) {
			errores.put("Fecha completa", "La fecha y hora deben tener el formato 'yyyy-MM-dd HH:mm'.");
		}

		if (!Validator.isValidRuta(ruta)) {
			errores.put("Ruta",
					"La ruta debe tener el formato 'Origen-Destino' o 'Origen-Destino1-Destino2', sin espacios y con un solo guion entre cada parte.");
		}

		if (!Validator.isValidPropietario(propietario)) {
			errores.put("Propietario",
					"El nombre del propietario debe tener el formato 'Nombre Apellido', con cada palabra comenzando con una letra mayúscula y separadas por un espacio.");
		}

		if (errores.isEmpty()) {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate fecha = LocalDate.parse(fechaString, dateFormatter);
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			LocalTime hora = LocalTime.parse(horaString, timeFormatter);
			fechaYHora = LocalDateTime.of(fecha, hora);

			if (!Validator.isBeforeDateTime(fechaYHora)) {
				errores.put("Fecha salida", "El viaje no debe ser anterior al día de hoy.");
			}
		}

		if (!errores.isEmpty()) {
			redirectAttributes.addFlashAttribute("errores", errores);
			return "redirect:/viaje/add";
		}

		try {
			Viaje viaje = new Viaje(0, propietario, ruta, fechaYHora, duracion, precio, plazasOfertadas);
			viajesRepository.save(viaje);
			redirectAttributes.addFlashAttribute("infoMensaje", "Viaje añadido con éxito");
			return "redirect:/";
		} catch (ViajeAlreadyExistsException | ViajeNotFoundException ex) {
			errores.put("codigo", ex.getMessage());
			redirectAttributes.addFlashAttribute("errores", errores);
			return "redirect:/";
		}
	}

	@GetMapping("/viaje")
	public String detalleViaje(@RequestParam int codViaje, Model model, RedirectAttributes redirectAttributes) {
		try {
			Viaje viaje = viajesRepository.getViajeByCodigo(codViaje);
			List<Reserva> reservas = viajesRepository.findAllByTravel(viaje);
			model.addAttribute("viaje", viaje);
			model.addAttribute("reservas", reservas);
			return "viaje/viaje_detalle";
		} catch (ViajeNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errores", "No se ha encontrado el viaje con código " + codViaje);
			return "redirect:/";
		}
	}

	@GetMapping("/viaje/cancel")
	public String cancelarViaje(@RequestParam int codViaje, RedirectAttributes redirectAttributes) {
		try {
			viajesRepository.cancelarViaje(codViaje);
			redirectAttributes.addFlashAttribute("infoMensaje", "El viaje ha sido cancelado exitosamente.");
		} catch (ViajeNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errores", "El viaje no se ha encontrado.");
			return "redirect:/";
		} catch (ViajeNotCancelableException ex) {
			redirectAttributes.addFlashAttribute("errores", "El viaje no se puede cancelar.");
			return "redirect:/";
		}
		return "redirect:/";
	}

}
