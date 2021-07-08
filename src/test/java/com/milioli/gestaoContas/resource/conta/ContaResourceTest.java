package com.milioli.gestaoContas.resource.conta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milioli.gestaoContas.BaseResourceTest;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;
import com.milioli.gestaoContas.model.model.conta.ContaTestUtils;
import com.milioli.gestaoContas.service.conta.ContaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.milioli.gestaoContas.model.model.pessoa.PessoaTestUtils.constroiPessoa;
import static com.milioli.gestaoContas.model.model.transacao.TransacaoTestUtils.constroiTransacao;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = ContaResource.class)
public class ContaResourceTest extends BaseResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ContaService service;

    public static String API_PATH = "/api/contas/";

    @Test
    public void deveCriarConta() throws Exception {

        final Pessoa pessoa = constroiPessoa(1L);
        final Conta conta = ContaTestUtils.constroiConta(null, pessoa);
        final ContaDto contaDto = ContaDto.toDto(conta);

        final Conta contaCriada = ContaTestUtils.constroiConta(1L, pessoa);

        Mockito.when(service.criar(any())).thenReturn(contaCriada);

        String json = new ObjectMapper().writeValueAsString(contaDto);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty());
    }

    @Test
    public void deveDepositarNaConta() throws Exception {

        final Pessoa pessoa = constroiPessoa(1L);
        final Conta conta = ContaTestUtils.constroiConta(1L, pessoa);

        final BigDecimal valor = BigDecimal.valueOf(100L);

        Mockito.when(service.depositar(any(), any())).thenReturn(conta);

        String json = new ObjectMapper().writeValueAsString(valor);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API_PATH.concat(conta.getId().toString()).concat("/depositar"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    public void deveSacarNaConta() throws Exception {

        final Pessoa pessoa = constroiPessoa(1L);
        final Conta conta = ContaTestUtils.constroiConta(1L, pessoa);

        final BigDecimal valor = BigDecimal.valueOf(100L);

        Mockito.when(service.sacar(any(), any())).thenReturn(conta);

        String json = new ObjectMapper().writeValueAsString(valor);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API_PATH.concat(conta.getId().toString()).concat("/sacar"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    public void deveBloquearConta() throws Exception {

        final Pessoa pessoa = constroiPessoa(1L);
        final Conta conta = ContaTestUtils.constroiConta(1L, pessoa);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API_PATH.concat(conta.getId().toString()).concat("/bloquear"))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    public void deveListarTransacoesConta() throws Exception {
        final Pessoa pessoa = constroiPessoa(1L);
        final Conta conta = ContaTestUtils.constroiConta(1L, pessoa);

        final Transacao primeiraTransacao = constroiTransacao(1L, conta);
        final Transacao segundaTransacao = constroiTransacao(2L, conta);

        final List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(primeiraTransacao);
        transacoes.add(segundaTransacao);

        Mockito.when(service.getById(any())).thenReturn(conta);
        Mockito.when(service.transacoes(any(), any(), any())).thenReturn(transacoes);

        final LocalDate from = LocalDate.of(2017, 1, 1);
        final LocalDate to = LocalDate.now();

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API_PATH.concat(conta.getId().toString()).concat("/transacoes"))
                .accept(MediaType.APPLICATION_JSON)
                .param("from", from.toString())
                .param("to", to.toString());

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(primeiraTransacao.getId()));
    }

}
