package com.milioli.gestaoContas.model.model.conta;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;

public class ContaTest extends BaseTest {

    public Pessoa persisteERetornaPessoa() {
        final Pessoa pessoa = constroiPessoa(null);
        return entityManager.persist(pessoa);
    }

    @Test
    public void devePersistirConta() {
        Conta conta = new Conta(
                null,
                persisteERetornaPessoa(),
                ContaTestUtils.SALDO,
                ContaTestUtils.LIMITE_SAQUE_DIARIO,
                ContaTestUtils.ATIVO,
                ContaTestUtils.TIPO_CONTA,
                ContaTestUtils.DATA_CRIACAO
        );

        final Conta persisted = entityManager.persist(conta);

        Assertions.assertThat(persisted).isNotNull();
    }

    @Test
    public void deveRetornarErroAoTentarPersistirContaSemPessoa() {
        final Conta conta = new Conta(
                null,
                null,
                ContaTestUtils.SALDO,
                ContaTestUtils.LIMITE_SAQUE_DIARIO,
                ContaTestUtils.ATIVO,
                ContaTestUtils.TIPO_CONTA,
                ContaTestUtils.DATA_CRIACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe uma pessoa");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirContaSemSaldo() {
        final Conta conta = new Conta(
                null,
                persisteERetornaPessoa(),
                null,
                ContaTestUtils.LIMITE_SAQUE_DIARIO,
                ContaTestUtils.ATIVO,
                ContaTestUtils.TIPO_CONTA,
                ContaTestUtils.DATA_CRIACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe um saldo");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirContaSemSaqueDiario() {
        final Conta conta = new Conta(
                null,
                persisteERetornaPessoa(),
                ContaTestUtils.SALDO,
                null,
                ContaTestUtils.ATIVO,
                ContaTestUtils.TIPO_CONTA,
                ContaTestUtils.DATA_CRIACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe um limite de saque diário");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirContaSemFlagAtivo() {
        final Conta conta = new Conta(
                null,
                persisteERetornaPessoa(),
                ContaTestUtils.SALDO,
                ContaTestUtils.LIMITE_SAQUE_DIARIO,
                null,
                ContaTestUtils.TIPO_CONTA,
                ContaTestUtils.DATA_CRIACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe se a conta está ativa");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirContaSemTipoConta() {
        final Conta conta = new Conta(
                null,
                persisteERetornaPessoa(),
                ContaTestUtils.SALDO,
                ContaTestUtils.LIMITE_SAQUE_DIARIO,
                ContaTestUtils.ATIVO,
                null,
                ContaTestUtils.DATA_CRIACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe o tipo de conta");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirContaSemDataCriacao() {
        final Conta conta = new Conta(
                null,
                persisteERetornaPessoa(),
                ContaTestUtils.SALDO,
                ContaTestUtils.LIMITE_SAQUE_DIARIO,
                ContaTestUtils.ATIVO,
                ContaTestUtils.TIPO_CONTA,
                null
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(conta));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe a data de criação");
    }

}