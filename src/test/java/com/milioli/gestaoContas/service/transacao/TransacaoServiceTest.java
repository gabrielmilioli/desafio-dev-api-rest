package com.milioli.gestaoContas.service.transacao;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.enums.TipoTransacaoEnum;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import com.milioli.gestaoContas.model.repository.transacao.TransacaoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.milioli.gestaoContas.model.model.conta.ContaTestUtils.constroiConta;
import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;
import static com.milioli.gestaoContas.model.model.transacao.TransacaoTestUtils.VALOR;
import static com.milioli.gestaoContas.model.model.transacao.TransacaoTestUtils.constroiTransacao;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class TransacaoServiceTest extends BaseTest {

    @SpyBean
    TransacaoServiceImp service;

    @MockBean
    TransacaoRepository repository;

    public Transacao persisteERetornaTransacao() {
        final Pessoa PESSOA = entityManager.persist(constroiPessoa(null));
        final Conta CONTA = entityManager.persist(constroiConta(null, PESSOA));

        final Transacao transacao = constroiTransacao(null, CONTA);
        return entityManager.persist(transacao);
    }

    @Test
    public void deveObterTransacaoPorId() {
        final Transacao transacao = persisteERetornaTransacao();
        Mockito.when(repository.getById(anyLong())).thenReturn(transacao);
        final Transacao byId = service.getById(transacao.getId());
        Assertions.assertThat(byId).isNotNull();
    }

    @Test
    public void deveObterTransacoesPorConta() {
        final Transacao transacao = persisteERetornaTransacao();
        final List<Transacao> transacoes = Collections.singletonList(transacao);

        Mockito.when(repository.getByConta(any())).thenReturn(transacoes);
        final List<Transacao> transacoesConta = service.getByConta(transacao.getConta());

        Assertions.assertThat(transacoesConta.size()).isEqualTo(transacoes.size());
    }

    @Test
    public void deveObterValorSaqueTransacoesByContaAndDataTransacao() {
        final Transacao transacao = persisteERetornaTransacao();

        Mockito.when(repository.obterValorSaqueTransacoesByContaAndDataTransacao(any(), any())).thenReturn(transacao.getValor());
        final BigDecimal valorSaqueTransacoes = service.obterValorSaqueTransacoesByContaAndDataTransacao(transacao.getConta(), LocalDate.now());

        Assertions.assertThat(valorSaqueTransacoes).isEqualTo(transacao.getValor());
    }

    @Test
    public void deveRegistrarTransacao() {
        final Pessoa PESSOA = entityManager.persist(constroiPessoa(null));
        final Conta CONTA = entityManager.persist(constroiConta(null, PESSOA));
        final BigDecimal valor = VALOR;
        final TipoTransacaoEnum tipoTransacao = TipoTransacaoEnum.SAQUE;

        service.registraTransacao(CONTA, valor, tipoTransacao);

        Mockito.verify(repository).save(any());
    }

}
