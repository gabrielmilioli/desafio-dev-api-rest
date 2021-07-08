package com.milioli.gestaoContas.service.conta;

import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ContaService {

    Conta getById(Long id);

    Conta criar(Conta conta);

    Conta sacar(Conta conta, BigDecimal valor);

    Conta depositar(Conta conta, BigDecimal valor);

    void bloquear(Conta conta);

    List<Transacao> transacoes(Conta conta, LocalDate from, LocalDate to);
}
