package com.milioli.gestaoContas.enums;

public enum TipoContaEnum {
    CONTA_CORRENTE(1, "Conta corrente"),
    CONTA_POUPANCA(2, "Conta poupança"),
    CONTA_SALARIO(3, "Conta salário"),
    CONTA_DIGITAL(4, "Conta digital"),
    CONTA_UNIVERSITARIA(5, "Conta universitária");

    private final Integer codigo;
    private final String descricao;

    TipoContaEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
