package com.proyecto.ecommerce.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioSerice;

	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario) {
		logger.info("Usuario registro: {}", usuario);
		usuario.setTipo("User");
		usuarioSerice.save(usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@PostMapping("/usuarioTemporal")
	private String usuarioTemporal(Usuario usuario, HttpSession session) {
		// logger.info("El usuario temporal: {}",usuario);

		Optional<Usuario> userTemp = usuarioSerice.findByCorreo(usuario.getCorreo());
		// logger.info("usuario tempotal: {}", userTemp.get());

		if (userTemp.isPresent()) {
			session.setAttribute("idusuario", userTemp.get().getId());
			if (userTemp.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			logger.info("El usuario no existe");
		}
		return "redirect:/";
	}
	
	@GetMapping("/obtenerCompras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("session", session.getAttribute("idusuario"));
		return"usuario/compras";
	}
	
	

}
