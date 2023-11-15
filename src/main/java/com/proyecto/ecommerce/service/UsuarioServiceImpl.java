package com.proyecto.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
		
	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> findByCorreo(String correo) {
		
		return usuarioRepository.findByCorreo(correo);
	}
	 
	

}
