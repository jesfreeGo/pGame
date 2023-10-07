package com.proyecto.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller  //controlador
@RequestMapping("/")       //se mapea a un punto en especifico de la logica dentro de la clase
public class HomeControllerUser {
	
	//Logica para mostrar productos al usuario
	
	@GetMapping("")   // se mapea con la home de html
	public String home() {
		return "usuario/home";
	}

}
