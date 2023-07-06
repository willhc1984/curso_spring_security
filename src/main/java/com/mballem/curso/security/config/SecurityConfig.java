package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.MEDICO.getDesc();
	
	@Autowired
	private UsuarioService service;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
			.antMatchers("/", "/home").permitAll()
			
			// acesso privados admin
			.antMatchers("/u/**").hasAuthority(ADMIN)
			
			//acessos privados médicos
			.antMatchers("/medicos/**").hasAuthority(MEDICO)
			
			//acessos privados especialidade
			.antMatchers("/especialidades/**").hasAuthority(ADMIN)
			
			//acessos privados pacientes
			.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
			
			.anyRequest().authenticated()
			
			.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login-error")
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.exceptionHandling()
				.accessDeniedPage("/acesso-negado");
		
	}

}
