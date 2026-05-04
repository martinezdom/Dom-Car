package com.domcar.controllers;

import com.domcar.exceptions.ReservaAlreadyExistsException;
import com.domcar.exceptions.ReservaNoValidaException;
import com.domcar.exceptions.ReservaNotFoundException;
import com.domcar.exceptions.ViajeNotFoundException;
import com.domcar.modelo.dto.Reserva;
import com.domcar.modelo.dto.viaje.Viaje;
import com.domcar.modelo.repositories.ViajesRepository;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservaController {

	@Autowired
	private ViajesRepository viajesRepository;

	@GetMapping("/viaje/reserva/add")
	public String reservaFormActionView(@RequestParam(required = false) Integer codViaje, Model model,
			RedirectAttributes redirectAttributes) {
		if (codViaje == null) {
			return "redirect:/";
		} else {
			try {
				viajesRepository.getViajeByCodigo(codViaje);
				model.addAttribute("codViaje", codViaje);
				return "reserva/reserva_form";
			} catch (ViajeNotFoundException e) {
				redirectAttributes.addFlashAttribute("errores", "El viaje no se ha encontrado.");
				return "redirect:/";
			}
		}
	}

	@PostMapping("/viaje/reserva/add")
	public String insertarReserva(@RequestParam HashMap<String, String> params, RedirectAttributes redirectAttributes) {
		String usuario = params.get("usuario");
		String plazasSolicitadasString = params.get("plazasSolicitadas");
		String codViajeString = params.get("codViaje");

		int plazasSolicitadas = 0;
		int codViaje = 0;
		HashMap<String, String> errores = new HashMap<>();

		try {
			codViaje = Integer.parseInt(codViajeString);
		} catch (NumberFormatException e) {
			errores.put("Error", "El código de viaje no es válido.");
			redirectAttributes.addFlashAttribute("errores", errores);
			return "redirect:/";
		}

		try {
			plazasSolicitadas = Integer.parseInt(plazasSolicitadasString);
		} catch (NumberFormatException e) {
			errores.put("Error", "Las plazas solicitadas deben ser un número válido.");
			redirectAttributes.addFlashAttribute("errores", errores);
			return "redirect:/viaje/reserva/add?codViaje=" + codViaje;
		}

		try {
			Viaje viaje = viajesRepository.findViajeSiPermiteReserva(codViaje, usuario, plazasSolicitadas);
			String codReserva = viajesRepository.getNextCodReserva(viaje);
			Reserva reserva = new Reserva(codReserva, usuario, plazasSolicitadas, viaje);
			viajesRepository.save(reserva);
			redirectAttributes.addFlashAttribute("infoMensaje", "Reserva añadida con éxito");
			return "redirect:/";
		} catch (ReservaAlreadyExistsException | ReservaNoValidaException | ViajeNotFoundException
				| ReservaNotFoundException ex) {
			errores.put("Error", ex.getMessage());
			redirectAttributes.addFlashAttribute("errores", errores);
			return "redirect:/viaje/reserva/add?codViaje=" + codViaje;
		}
	}

	@GetMapping("/viaje/reservas")
	public String reservasDeUnViaje(@RequestParam(required = false) Integer codViaje, Model model,
			RedirectAttributes redirectAttributes) {
		if (codViaje == null) {
			return "redirect:/";
		} else {
			try {
				Viaje viaje = viajesRepository.getViajeByCodigo(codViaje);
				List<Reserva> reservas = viajesRepository.findReservasByViaje(viaje);
				model.addAttribute("reservas", reservas);
				model.addAttribute("codViaje", codViaje);
				return "reserva/listado";
			} catch (ViajeNotFoundException ex) {
				redirectAttributes.addFlashAttribute("errores", ex.getMessage());
				return "redirect:/";
			}
		}
	}

	@GetMapping("/viaje/reserva")
	public String detalleReserva(@RequestParam String codReserva, Model model, RedirectAttributes redirectAttributes) {
		try {
			Reserva reserva = viajesRepository.getReservaById(codReserva);
			model.addAttribute("reserva", reserva);
			return "reserva/reserva_detalle";
		} catch (ReservaNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errores", "La reserva no se ha encontrado.");
			return "redirect:/";
		}
	}

	@GetMapping("/viaje/reserva/del")
	public String eliminarReserva(@RequestParam String codReserva, Model model, RedirectAttributes redirectAttributes) {
		try {
			Reserva reserva = viajesRepository.getReservaById(codReserva);
			viajesRepository.remove(reserva);
			redirectAttributes.addFlashAttribute("infoMensaje", "La reserva ha sido cancelada exitosamente.");
			return "redirect:/viaje?codViaje=" + reserva.getViaje().getCodViaje();
		} catch (ReservaNotFoundException ex) {
			redirectAttributes.addFlashAttribute("errores", "La reserva no se ha encontrado.");
			return "redirect:/";
		}
	}

}
