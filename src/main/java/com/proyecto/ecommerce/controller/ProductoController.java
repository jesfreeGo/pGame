package com.proyecto.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.service.IUsuarioService;
import com.proyecto.ecommerce.service.ProductoService;
import com.proyecto.ecommerce.service.UploadFileService;

@Controller

@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	@Autowired // anotacion de spring, inyeccion de dependencia
	private ProductoService productoService;
	@Autowired
	private UploadFileService upload;
	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("") // se mapea con el paquete modelo a la clase producto
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {
		LOGGER.info("Este es el objeto de la vista{}", producto);

		Usuario u1 = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(u1);

		// imagen, producto nuevo
		if (producto.getId() == null) {// cuando se crea un producto
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {

		}

		productoService.save(producto);
		return "redirect:/productos";
	}

	@GetMapping("edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();

		LOGGER.info("Producto buscado {}", producto);

		model.addAttribute("producto", producto);

		return "productos/edit";
	}

	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();

		if (file.isEmpty()) {

			producto.setImagen(p.getImagen());
		} else {// cuando se edita la imagen

			// elimina cuando no sea la imagen por defecto
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}

			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);

		return "redirect:/productos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {

		Producto p = new Producto();
		p = productoService.get(id).get();
		// elimina cuando no sea la imagen por defecto
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}

		productoService.delete(id);
		return "redirect:/productos";
	}
}
