package com.milioli.gestaoContas.resource.conta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.milioli.gestaoContas.enums.SimNaoEnum;
import com.milioli.gestaoContas.enums.TipoContaEnum;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.resource.pessoa.PessoaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDto {

    private Long id;

    private PessoaDto pessoa;

    private BigDecimal saldo;

    private BigDecimal limiteSaqueDiario;

    @Enumerated(value = EnumType.STRING)
    private SimNaoEnum ativo;

    @Enumerated(value = EnumType.ORDINAL)
    private TipoContaEnum tipoConta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataCriacao;

    public static ContaDto toDto(Conta conta) {
        return new ContaDto(
                conta.getId(),
                PessoaDto.toDto(conta.getPessoa()),
                conta.getSaldo(),
                conta.getLimiteSaqueDiario(),
                conta.getAtivo(),
                conta.getTipoConta(),
                conta.getDataCriacao()
        );
    }

    public static Conta toEntity(ContaDto conta) {
        return new Conta(
                conta.getId(),
                PessoaDto.toEntity(conta.getPessoa()),
                conta.getSaldo(),
                conta.getLimiteSaqueDiario(),
                conta.getAtivo(),
                conta.getTipoConta(),
                conta.getDataCriacao()
        );
    }

}
