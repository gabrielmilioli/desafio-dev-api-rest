package com.milioli.gestaoContas.model.repository.pessoa;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;

public class PessoaRepositoryTest extends BaseTest {

    @Autowired
    PessoaRepository repository;

    public Pessoa persisteERetornaPessoa() {
        final Pessoa pessoa = constroiPessoa(null);
        return entityManager.persist(pessoa);
    }

    @Test
    public void deveObterUmaPessoaPorId() {
        final Pessoa pessoa = persisteERetornaPessoa();

        final Pessoa entity = repository.getById(pessoa.getId());

        Assertions.assertThat(entity).isNotNull();
    }

}
