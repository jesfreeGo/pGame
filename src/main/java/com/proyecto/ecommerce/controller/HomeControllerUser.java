package com.proyecto.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.ecommerce.model.DetalleOrden;
import com.proyecto.ecommerce.model.Orden;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.service.IDetalleOrdenService;
import com.proyecto.ecommerce.service.IOrdenService;
import com.proyecto.ecommerce.service.IUsuarioService;
import com.proyecto.ecommerce.service.ProductoService;

@Controller // controlador
@RequestMapping("/") // se mapea a un punto en especifico (raiz) de la logica dentro de la clase
public class HomeControllerUser {

	private final Logger log = LoggerFactory.getLogger(HomeControllerUser.class);

	@Autowired // inyeccion de dependencias
	private ProductoService productoService;// nos permite obtener los productos de la DB
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private IOrdenService ordenService;
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>(); // almacenar los detalles de la orden
	Orden orden = new Orden(); // datos de la orden

	// Logica para mostrar productos al usuario

	@GetMapping("") // se mapea con la home de html
	public String home(Model model, HttpSession session) {
		// log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));

		model.addAttribute("productos", productoService.findAll());

		// session
		model.addAttribute("session", session.getAttribute("idusuario"));

		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {

		log.info("Id del producto enviado como parametro {}", id);
		Producto producto = new Producto(); // nuevo objeto de tipo producto
		Optional<Producto> productoOptional = productoService.get(id); // parecido a una lista pero de tipo optional,
																		// nos traemos el producto por id
		producto = productoOptional.get();
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCar(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("producto anadido {}", optionalProducto.get());
		log.info("cantidad {}", cantidad);
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validar que el producto no se vuelva a agregar de nuevo
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

		// validar boleano
		if (!ingresado) {
			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	// elimina producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductCart(@PathVariable Integer id, Model model) {// model envia info a la vista

		// lista nueva de productos
		List<DetalleOrden> ordenenesNueva = new ArrayList<DetalleOrden>();

		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenenesNueva.add(detalleOrden);
			}
		}

		// regresa la nueva lista sin el producto eliminado
		detalles = ordenenesNueva;

		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/goCart")
	public String goCart(Model model, HttpSession session) {

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		// session
		model.addAttribute("session", session.getAttribute("idusuario"));

		return "usuario/carrito";
	}

	@GetMapping("/order")
	public String order(Model model, HttpSession session) {

		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);

		return "usuario/resumenorden";
	}

	// guarda la orden
	@GetMapping("/saveOrder")
	private String saveOrder(HttpSession session) {

		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());

		// usuario
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		orden.setUsuario(usuario);
		ordenService.save(orden);

		// guardar los detalles
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}

		// limpia lista y orden
		orden = new Orden();
		detalles.clear();

		return "redirect:/";
	}

	@PostMapping("/search")
	public String search(@RequestParam String nombre, Model model) {

		log.info("Nombfre del producto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre))
				.collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "usuario/home";
	}

}
