package com.milioli.gestaoContas.model.model.transacao;

import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.model.entity.transacao.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacaoTestUtils {

    public static Long ID = 1L;
    public static BigDecimal VALOR = BigDecimal.valueOf(100L).setScale(2);
    public static LocalDate DATA_TRANSACAO = LocalDate.now();

    public static Transacao constroiTransacao(Long id, Conta conta) {
        return new Transacao(
                id,
                conta,
                VALOR,
                DATA_TRANSACAO);
    }

}
