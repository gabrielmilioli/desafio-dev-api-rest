package com.milioli.gestaoContas.model.repository.transacao;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.milioli.gestaoContas.model.model.conta.ContaTestUtils.constroiConta;
import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;
import static com.milioli.gestaoContas.model.model.transacao.TransacaoTestUtils.constroiTransacao;

public class TransacaoRepositoryTest extends BaseTest {

    @Autowired
    TransacaoRepository repository;

    public Transacao persisteERetornaTransacao() {
        final Pessoa PESSOA = entityManager.persist(constroiPessoa(null));
        final Conta CONTA = entityManager.persist(constroiConta(null, PESSOA));

        final Transacao transacao = constroiTransacao(null, CONTA);
        return entityManager.persist(transacao);
    }

    @Test
    public void deveSalvarUmaTransacao() {
        final Pessoa PESSOA = entityManager.persist(constroiPessoa(null));
        final Conta CONTA = entityManager.persist(constroiConta(null, PESSOA));

        final Transacao transacao = constroiTransacao(null, CONTA);

        final Transacao entity = repository.save(transacao);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isNotNull();
    }

    @Test
    public void deveObterUmaTransacaoPorId() {
        final Transacao transacao = persisteERetornaTransacao();

        final Transacao entity = repository.getById(transacao.getId());

        Assertions.assertThat(entity).isNotNull();
    }

    @Test
    public void deveFiltrarTransacaoPorConta() {
        final Transacao transacao = persisteERetornaTransacao();

        final List<Transacao> transacoes = repository.getByConta(transacao.getConta());

        Assertions.assertThat(transacoes.size()).isEqualTo(1);
    }

    @Test
    public void deveObterValorSaqueTransacoesPorContaEDataTransacao() {
        final Pessoa PESSOA = entityManager.persist(constroiPessoa(null));
        final Conta CONTA = entityManager.persist(constroiConta(null, PESSOA));

        final Transacao primeiraTransacao = constroiTransacao(null, CONTA);
        primeiraTransacao.setValor(BigDecimal.valueOf(100L).negate());
        repository.save(primeiraTransacao);

        final Transacao segundaTransacao = constroiTransacao(null, CONTA);
        segundaTransacao.setValor(BigDecimal.valueOf(100L).negate());
        repository.save(segundaTransacao);

        final Transacao terceiraTransacao = constroiTransacao(null, CONTA);
        terceiraTransacao.setValor(BigDecimal.valueOf(100L).negate());
        repository.save(terceiraTransacao);

        final List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(primeiraTransacao);
        transacoes.add(segundaTransacao);
        transacoes.add(terceiraTransacao);
        final BigDecimal total = transacoes.stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);

        final BigDecimal valorSaqueTransacoes = repository.obterValorSaqueTransacoesByContaAndDataTransacao(CONTA.getId(),
                LocalDate.now());

        Assertions.assertThat(valorSaqueTransacoes).isEqualTo(total);
    }

}
