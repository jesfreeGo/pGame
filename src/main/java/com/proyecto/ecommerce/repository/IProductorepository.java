package com.proyecto.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.ecommerce.model.Producto;

@Repository   //repositorio de datos de spring data interactua con una base de datos de JPA
public interface IProductorepository extends JpaRepository<Producto,Integer>{  //interface "Productorepository" extiende de "JpaRepository"
	

}
