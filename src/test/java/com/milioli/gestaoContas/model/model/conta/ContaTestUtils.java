package com.milioli.gestaoContas.model.model.conta;

import com.milioli.gestaoContas.enums.SimNaoEnum;
import com.milioli.gestaoContas.enums.TipoContaEnum;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaTestUtils {

    public static Long ID = 1L;
    public static BigDecimal SALDO = BigDecimal.valueOf(100L);
    public static BigDecimal LIMITE_SAQUE_DIARIO = BigDecimal.valueOf(500L);
    public static SimNaoEnum ATIVO = SimNaoEnum.SIM;
    public static TipoContaEnum TIPO_CONTA = TipoContaEnum.CONTA_CORRENTE;
    public static LocalDate DATA_CRIACAO = LocalDate.of(2021, 7, 7);

    public static Conta constroiConta(Long id, Pessoa pessoa) {
        return new Conta(
                id,
                pessoa,
                SALDO,
                LIMITE_SAQUE_DIARIO,
                ATIVO,
                TIPO_CONTA,
                DATA_CRIACAO);
    }

}
