package com.mballem.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({"/", "/home"})
	public String home() {
		return "home";
	}	
	
	//abrir pagina login
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	//login invalido
	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Login ou senha invalidos, tente novamente.");
		model.addAttribute("alerta", "erro");
		model.addAttribute("subtexto", "Acesso permitido somente para cadastro já ativado.");
		return "login";
	}
}
