package com.milioli.gestaoContas.service.pessoa;

import com.milioli.gestaoContas.BaseTest;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import com.milioli.gestaoContas.model.repository.pessoa.PessoaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;
import static org.mockito.ArgumentMatchers.anyLong;

public class PessoaServiceTest extends BaseTest {

    @SpyBean
    PessoaServiceImp service;

    @MockBean
    PessoaRepository repository;

    public Pessoa persisteERetornaPessoa() {
        final Pessoa pessoa = constroiPessoa(null);
        return entityManager.persist(pessoa);
    }

    @Test
    public void deveObterPessoaPorId() {
        final Pessoa pessoa = persisteERetornaPessoa();
        Mockito.when(repository.getById(anyLong())).thenReturn(pessoa);
        final Pessoa byId = service.getById(pessoa.getId());
        Assertions.assertThat(byId).isNotNull();
    }

}
