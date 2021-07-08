package com.milioli.gestaoContas.service.conta;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.enums.SimNaoEnum;
import com.milioli.gestaoContas.exception.RegraNegocioException;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import com.milioli.gestaoContas.model.repository.conta.ContaRepository;
import com.milioli.gestaoContas.model.repository.transacao.TransacaoRepository;
import com.milioli.gestaoContas.service.transacao.TransacaoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.milioli.gestaoContas.model.model.conta.ContaTestUtils.LIMITE_SAQUE_DIARIO;
import static com.milioli.gestaoContas.model.model.conta.ContaTestUtils.constroiConta;
import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;
import static com.milioli.gestaoContas.model.model.transacao.TransacaoTestUtils.constroiTransacao;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class ContaServiceTest extends BaseTest {

    @SpyBean
    ContaServiceImp service;

    @MockBean
    ContaRepository repository;

    @MockBean
    TransacaoService transacaoService;

    @MockBean
    TransacaoRepository transacaoRepository;

    public Conta persisteERetornaConta() {
        final Conta conta = constroiConta(null, entityManager.persist(constroiPessoa(null)));
        return entityManager.persist(conta);
    }

    @Test
    public void deveObterContaPorId() {
        final Conta conta = persisteERetornaConta();
        Mockito.when(repository.getById(anyLong())).thenReturn(conta);
        final Conta byId = service.getById(conta.getId());
        Assertions.assertThat(byId).isNotNull();
    }

    @Test
    public void deveCriarConta() {
        final Conta contaPersistida = constroiConta(1L, entityManager.persist(constroiPessoa(null)));
        final Conta conta = constroiConta(null, entityManager.persist(constroiPessoa(null)));

        Mockito.when(repository.existsByPessoaAndTipoConta(any(), any())).thenReturn(Boolean.FALSE);
        Mockito.when(repository.save(any())).thenReturn(contaPersistida);

        final Conta created = service.criar(conta);
        Assertions.assertThat(created).isNotNull();
    }

    @Test
    public void deveRetornarErroAoTentarCriarContaComAMesmaPessoaETipoConta() {
        final Conta conta = constroiConta(null, entityManager.persist(constroiPessoa(null)));

        Mockito.when(repository.existsByPessoaAndTipoConta(any(), any())).thenReturn(Boolean.TRUE);

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.criar(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Tipo de conta já existente");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveSacarValorNaConta() {
        final BigDecimal valor = BigDecimal.valueOf(100L);

        final Conta conta = persisteERetornaConta();

        final Conta contaAtualizada = persisteERetornaConta();
        final BigDecimal novoSaldo = contaAtualizada.getSaldo().subtract(valor);
        contaAtualizada.setSaldo(novoSaldo);

        Mockito.when(transacaoService.obterValorSaqueTransacoesByContaAndDataTransacao(any(), any())).thenReturn(BigDecimal.ZERO);
        Mockito.when(repository.save(any())).thenReturn(contaAtualizada);
        Mockito.doNothing().when(transacaoService).registraTransacao(any(), any(), any());

        final Conta contaPosSaque = service.sacar(conta, valor);
        Assertions.assertThat(contaPosSaque).isNotNull();
        Assertions.assertThat(contaPosSaque.getSaldo()).isEqualTo(novoSaldo);
    }

    @Test
    public void deveRetornarErroAoTentarSacarValorNaContaComContaBloqueada() {
        final BigDecimal valor = BigDecimal.valueOf(100L);

        final Conta conta = persisteERetornaConta();
        conta.setAtivo(SimNaoEnum.NAO);

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.sacar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Conta bloqueada");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarSacarValorNaContaComValorZerado() {
        final BigDecimal valor = BigDecimal.ZERO;

        final Conta conta = persisteERetornaConta();

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.sacar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Valor inválido");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarSacarValorNaContaComValorNegativo() {
        final BigDecimal valor = BigDecimal.valueOf(100L).negate();

        final Conta conta = persisteERetornaConta();

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.sacar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Valor inválido");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarSacarValorNaContaComSaldoInsuficiente() {
        final BigDecimal valor = BigDecimal.valueOf(100L);

        final Conta conta = persisteERetornaConta();
        conta.setSaldo(BigDecimal.ZERO);

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.sacar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Saldo insuficiente");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarSacarValorNaContaComLimiteSaqueDiarioExcedido() {
        final BigDecimal valor = BigDecimal.valueOf(100L);

        final Conta conta = persisteERetornaConta();

        Mockito.when(transacaoService.obterValorSaqueTransacoesByContaAndDataTransacao(any(), any())).thenReturn(LIMITE_SAQUE_DIARIO.negate());

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.sacar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Limite de saques diário excedido");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveDepositarValorNaConta() {
        final BigDecimal valor = BigDecimal.valueOf(100L);

        final Conta conta = persisteERetornaConta();

        final Conta contaAtualizada = persisteERetornaConta();
        final BigDecimal novoSaldo = contaAtualizada.getSaldo().add(valor);
        contaAtualizada.setSaldo(novoSaldo);

        Mockito.when(repository.save(any())).thenReturn(contaAtualizada);
        Mockito.doNothing().when(transacaoService).registraTransacao(any(), any(), any());

        final Conta contaPosDeposito = service.depositar(conta, valor);
        Assertions.assertThat(contaPosDeposito).isNotNull();
        Assertions.assertThat(contaPosDeposito.getSaldo()).isEqualTo(novoSaldo);
    }

    @Test
    public void deveRetornarErroAoTentarDepositarValorNaContaComContaBloqueada() {
        final BigDecimal valor = BigDecimal.valueOf(100L);

        final Conta conta = persisteERetornaConta();
        conta.setAtivo(SimNaoEnum.NAO);

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.depositar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Conta bloqueada");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarDepositarValorNaContaComValorZerado() {
        final BigDecimal valor = BigDecimal.ZERO;

        final Conta conta = persisteERetornaConta();

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.depositar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Valor inválido");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarDepositarValorNaContaComValorNegativo() {
        final BigDecimal valor = BigDecimal.valueOf(100L).negate();

        final Conta conta = persisteERetornaConta();

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.depositar(conta, valor));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Valor inválido");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveBloquearConta() {
        final Conta conta = persisteERetornaConta();

        service.bloquear(conta);

        Mockito.verify(repository).save(conta);
    }

    @Test
    public void deveRetornarErroAoTentarBloquearContaJaBloqueada() {
        final Conta conta = persisteERetornaConta();
        conta.setAtivo(SimNaoEnum.NAO);

        final Throwable throwable = Assertions.catchThrowable(() ->
                service.bloquear(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Conta bloqueada");

        Mockito.verify(repository, Mockito.never()).save(conta);
    }

    @Test
    public void deveListarTransacoesDaConta() {
        final Conta conta = persisteERetornaConta();

        final Transacao primeiraTransacao = constroiTransacao(null, conta);
        transacaoRepository.save(primeiraTransacao);

        final Transacao segundaTransacao = constroiTransacao(null, conta);
        transacaoRepository.save(segundaTransacao);

        final List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(primeiraTransacao);
        transacoes.add(segundaTransacao);

        Mockito.when(transacaoService.getByConta(any())).thenReturn(transacoes);

        final List<Transacao> transacoesPorConta = service.transacoes(conta, null, null);
        Assertions.assertThat(transacoesPorConta.size()).isEqualTo(transacoes.size());
    }

    @Test
    public void deveListarTransacoesDaContaPorPeriodo() {
        final Conta conta = persisteERetornaConta();
        final LocalDate dateFrom = LocalDate.of(2020, 1, 1);
        final LocalDate dateTo = LocalDate.now();

        final Transacao primeiraTransacao = constroiTransacao(null, conta);
        primeiraTransacao.setDataTransacao(dateFrom);
        transacaoRepository.save(primeiraTransacao);

        final Transacao segundaTransacao = constroiTransacao(null, conta);
        transacaoRepository.save(segundaTransacao);

        final List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(primeiraTransacao);
        transacoes.add(segundaTransacao);

        Mockito.when(transacaoService.getByConta(any())).thenReturn(transacoes);

        final List<Transacao> transacoesPorConta = service.transacoes(conta, dateFrom, dateTo);
        Assertions.assertThat(transacoesPorConta.size()).isEqualTo(transacoes.size());
    }

}
