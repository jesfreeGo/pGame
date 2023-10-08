package com.proyecto.ecommerce.controller;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.service.ProductoService;

@Controller  //controlador
@RequestMapping("/")       //se mapea a un punto en especifico (raiz) de la logica dentro de la clase
public class HomeControllerUser {
	
	private final Logger log = LoggerFactory.getLogger(HomeControllerUser.class);
	
	@Autowired    //inyeccion de dependencias 
	private ProductoService productoService;//nos permite obtener los productos de la DB
	
	//Logica para mostrar productos al usuario
	
	@GetMapping("")   // se mapea con la home de html
	public String home(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		
		log.info("Id del producto enviado como parametro {}", id);
		Producto producto =  new Producto();  // nuevo objeto de tipo producto
		Optional<Producto> productoOptional = productoService.get(id); //parecido a una lista pero de tipo optional, nos traemos el producto por id
		producto = 	productoOptional.get();
		model.addAttribute("producto",producto);
		return"usuario/productohome";
	}

}
