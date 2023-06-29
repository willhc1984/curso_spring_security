package com.mballem.curso.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Usuario;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long>{
	

}
