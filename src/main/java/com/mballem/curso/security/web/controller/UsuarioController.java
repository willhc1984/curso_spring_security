package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	//abrir cadastro de usuarios (medico/paciente/admin)
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		return "usuario/cadastro";
	}
	
	//abrir lista de usuarios
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}
	
	//abrir lista de usuarios
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	
	// salvar cadastro de usuarios por administrador
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();
		if(perfis.size() > 2 || 
				perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
				perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))){
					attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico.");
					attr.addFlashAttribute("usuario", usuario);
		}else {
			try {
				service.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
			} catch (DataIntegrityViolationException e) {
				attr.addFlashAttribute("falha", "Cadastro não realizado. Email já existente!");
			}			
		}
			
		return "redirect:/u/novo/cadastro/usuario";
	}
	
	// pre edição de credenciais de usuarios
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable Long id) {
		return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
	}
	
	
	//pre edição de cadastro de usuarios
	@GetMapping("/editar/dados/usuario/{usuarioId}/perfis/{perfisId}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable Long usuarioId,
														@PathVariable Long[] perfisId) {
		
		Usuario us = service.buscarPorIdEPerfis(usuarioId, perfisId);
		
		if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
				!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){
			return new ModelAndView("usuario/cadastro", "usuario", us);
			
		} else if(us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){
			return new ModelAndView("especialidade/especialidade");
			
		} else if(us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Area restrita.");
			model.addObject("message", "Os dados de pacientes são restritos a ele.");
			return model;
		}
		
		return new ModelAndView("redirect:/u/lista");
	}
	
	

}
