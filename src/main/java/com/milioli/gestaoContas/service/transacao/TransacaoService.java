package com.milioli.gestaoContas.service.transacao;

import com.milioli.gestaoContas.enums.TipoTransacaoEnum;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoService {

    Transacao getById(Long id);

    List<Transacao> getByConta(Conta conta);

    BigDecimal obterValorSaqueTransacoesByContaAndDataTransacao(Conta conta, LocalDate dataTransacao);

    void registraTransacao(Conta conta, BigDecimal valor, TipoTransacaoEnum tipoTransacaoEnum);

}
