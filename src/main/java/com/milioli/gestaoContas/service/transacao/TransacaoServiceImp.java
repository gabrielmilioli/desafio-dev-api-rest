package com.milioli.gestaoContas.service.transacao;

import com.milioli.gestaoContas.enums.TipoTransacaoEnum;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import com.milioli.gestaoContas.model.repository.transacao.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoServiceImp implements TransacaoService {

    @Autowired
    TransacaoRepository repository;

    @Override
    public Transacao getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Transacao> getByConta(Conta conta) {
        return repository.getByConta(conta);
    }

    @Override
    public BigDecimal obterValorSaqueTransacoesByContaAndDataTransacao(Conta conta, LocalDate dataTransacao) {
        final BigDecimal valorSaqueTransacoes = repository.obterValorSaqueTransacoesByContaAndDataTransacao(conta.getId(), dataTransacao);
        return Optional.ofNullable(valorSaqueTransacoes).orElse(BigDecimal.ZERO).setScale(2);
    }

    @Override
    public void registraTransacao(Conta conta, BigDecimal valor, TipoTransacaoEnum tipoTransacaoEnum) {
        if (TipoTransacaoEnum.SAQUE.equals(tipoTransacaoEnum)) {
            final Transacao transacao = new Transacao(null, conta, valor.negate(), LocalDate.now());
            repository.save(transacao);
            return;
        }

        final Transacao transacao = new Transacao(null, conta, valor, LocalDate.now());
        repository.save(transacao);
    }

}
