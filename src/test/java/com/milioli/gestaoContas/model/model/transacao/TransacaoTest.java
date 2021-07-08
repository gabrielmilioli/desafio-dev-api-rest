package com.milioli.gestaoContas.model.model.transacao;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static com.milioli.gestaoContas.model.model.conta.ContaTestUtils.constroiConta;
import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;

public class TransacaoTest extends BaseTest {

    public Pessoa persisteERetornaPessoa() {
        final Pessoa pessoa = constroiPessoa(null);
        return entityManager.persist(pessoa);
    }

    public Conta persisteERetornaConta() {
        final Conta conta = constroiConta(null, persisteERetornaPessoa());
        return entityManager.persist(conta);
    }

    @Test
    public void devePersistirTransacao() {
        Transacao transacao = new Transacao(
                null,
                persisteERetornaConta(),
                TransacaoTestUtils.VALOR,
                TransacaoTestUtils.DATA_TRANSACAO
        );

        final Transacao persisted = entityManager.persist(transacao);

        Assertions.assertThat(persisted).isNotNull();
    }

    @Test
    public void deveRetornarErroAoTentarPersistirTransacaoSemConta() {
        Transacao transacao = new Transacao(
                null,
                null,
                TransacaoTestUtils.VALOR,
                TransacaoTestUtils.DATA_TRANSACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(transacao));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe uma conta");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirTransacaoSemValor() {
        Transacao transacao = new Transacao(
                null,
                persisteERetornaConta(),
                null,
                TransacaoTestUtils.DATA_TRANSACAO
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(transacao));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe um valor");
    }

    @Test
    public void deveRetornarErroAoTentarPersistirTransacaoSemDataTransacao() {
        Transacao transacao = new Transacao(
                null,
                persisteERetornaConta(),
                TransacaoTestUtils.VALOR,
                null
        );

        final Throwable throwable = Assertions.catchThrowable(() ->
                entityManager.persist(transacao));

        Assertions.assertThat(throwable)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Informe a data de transação");
    }

}
