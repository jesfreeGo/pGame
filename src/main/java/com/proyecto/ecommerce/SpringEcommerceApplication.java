package com.proyecto.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//levantamiento de la aplicacion (excluye la condicion a la conexion a la base de datos)
@SpringBootApplication   //(exclude = DataSourceAutoConfiguration.class)
public class SpringEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEcommerceApplication.class, args);
	}

}
