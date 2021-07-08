package com.milioli.gestaoContas.model.repository.pessoa;

import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}

