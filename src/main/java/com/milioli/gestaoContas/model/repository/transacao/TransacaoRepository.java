package com.milioli.gestaoContas.model.repository.transacao;

import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> getByConta(Conta conta);

    @Query(value = "select sum(t.valor) from Transacao t join t.conta c " +
            "where c.id=:idConta and t.dataTransacao=:dataTransacao and t.valor < 0 " +
            "group by c")
    BigDecimal obterValorSaqueTransacoesByContaAndDataTransacao(@Param("idConta") Long idConta,
                                                                @Param("dataTransacao") LocalDate dataTransacao);
}