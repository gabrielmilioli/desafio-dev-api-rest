package com.milioli.gestaoContas.model.model.pessoa;

import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;

import java.time.LocalDate;

public class PessoaTestUtils {

    public static Long ID = 1L;
    public static String NOME = "Gabriel";
    public static String CPF = "77465970091";
    public static LocalDate DATA_NASCIMENTO = LocalDate.of(1996, 12, 13);

    public static Pessoa constroiPessoa(Long id) {
        return new Pessoa(
                id,
                PessoaTestUtils.NOME,
                PessoaTestUtils.CPF,
                PessoaTestUtils.DATA_NASCIMENTO
        );
    }

}
