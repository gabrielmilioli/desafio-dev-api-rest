package com.milioli.gestaoContas.model.repository.conta;

import com.milioli.gestaoContas.enums.TipoContaEnum;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Boolean existsByPessoaAndTipoConta(Pessoa pessoa, TipoContaEnum tipoContaEnum);

}