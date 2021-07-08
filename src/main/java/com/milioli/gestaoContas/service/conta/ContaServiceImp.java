package com.milioli.gestaoContas.service.conta;

import com.milioli.gestaoContas.enums.SimNaoEnum;
import com.milioli.gestaoContas.enums.TipoTransacaoEnum;
import com.milioli.gestaoContas.exception.RegraNegocioException;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import com.milioli.gestaoContas.model.repository.conta.ContaRepository;
import com.milioli.gestaoContas.service.transacao.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContaServiceImp implements ContaService {

    @Autowired
    ContaRepository repository;

    @Autowired
    TransacaoService transacaoService;

    @Override
    public Conta getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public Conta criar(Conta conta) {
        if (repository.existsByPessoaAndTipoConta(conta.getPessoa(), conta.getTipoConta())) {
            throw new RegraNegocioException("Tipo de conta já existente");
        }

        conta.setDataCriacao(LocalDate.now());
        return repository.save(conta);
    }

    @Override
    public Conta sacar(Conta conta, BigDecimal valor) {
        final BigDecimal novoSaldo = conta.getSaldo().subtract(valor);

        if (SimNaoEnum.NAO.equals(conta.getAtivo())) {
            throw new RegraNegocioException("Conta bloqueada");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor inválido");
        }

        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Saldo insuficiente");
        }

        final BigDecimal valorSaqueTransacoes = transacaoService
                .obterValorSaqueTransacoesByContaAndDataTransacao(conta, LocalDate.now());
        if (valorSaqueTransacoes.negate().compareTo(conta.getLimiteSaqueDiario()) >= 0) {
            throw new RegraNegocioException("Limite de saques diário excedido");
        }

        conta.setSaldo(novoSaldo);
        final Conta saved = repository.save(conta);

        transacaoService.registraTransacao(conta, valor, TipoTransacaoEnum.SAQUE);

        return saved;
    }

    @Override
    public Conta depositar(Conta conta, BigDecimal valor) {
        if (SimNaoEnum.NAO.equals(conta.getAtivo())) {
            throw new RegraNegocioException("Conta bloqueada");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor inválido");
        }

        conta.setSaldo(conta.getSaldo().add(valor));
        final Conta saved = repository.save(conta);

        transacaoService.registraTransacao(conta, valor, TipoTransacaoEnum.DEPOSITO);

        return saved;
    }

    @Override
    public void bloquear(Conta conta) {
        if (SimNaoEnum.NAO.equals(conta.getAtivo())) {
            throw new RegraNegocioException("Conta bloqueada");
        }

        conta.setAtivo(SimNaoEnum.NAO);

        repository.save(conta);
    }

    @Override
    public List<Transacao> transacoes(Conta conta, LocalDate from, LocalDate to) {
        final List<Transacao> transacoes = transacaoService.getByConta(conta);
        if (Objects.nonNull(from) && Objects.nonNull(to)) {
            return transacoes.stream().filter(t ->
                    (t.getDataTransacao().isAfter(from) || t.getDataTransacao().isEqual(from)) &&
                            (t.getDataTransacao().isBefore(to) || t.getDataTransacao().isEqual(to))
            ).collect(Collectors.toList());
        }
        if (Objects.nonNull(from)) {
            return transacoes.stream().filter(t ->
                    t.getDataTransacao().isAfter(from) || t.getDataTransacao().isEqual(from)
            ).collect(Collectors.toList());
        }
        if (Objects.nonNull(to)) {
            return transacoes.stream().filter(t ->
                    t.getDataTransacao().isBefore(to) || t.getDataTransacao().isEqual(to)
            ).collect(Collectors.toList());
        }
        return transacoes;
    }

}
