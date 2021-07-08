package com.milioli.gestaoContas.model.repository.conta;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.milioli.gestaoContas.model.model.conta.ContaTestUtils.constroiConta;
import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;

public class ContaRepositoryTest extends BaseTest {

    @Autowired
    ContaRepository repository;

    public Conta persisteERetornaConta() {
        final Conta conta = constroiConta(null, entityManager.persist(constroiPessoa(null)));
        return entityManager.persist(conta);
    }

    @Test
    public void deveSalvarUmaConta() {
        final Conta conta = constroiConta(null, entityManager.persist(constroiPessoa(null)));

        final Conta entity = repository.save(conta);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isNotNull();
    }

    @Test
    public void deveVerificarSeExisteContaPorPessoaETipoConta() {
        final Conta conta = constroiConta(null, entityManager.persist(constroiPessoa(null)));

        final Conta entity = repository.save(conta);

        final Boolean exists = repository.existsByPessoaAndTipoConta(entity.getPessoa(), entity.getTipoConta());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void deveObterUmaContaPorId() {
        final Conta conta = persisteERetornaConta();

        final Conta entity = repository.getById(conta.getId());

        Assertions.assertThat(entity).isNotNull();
    }

}
