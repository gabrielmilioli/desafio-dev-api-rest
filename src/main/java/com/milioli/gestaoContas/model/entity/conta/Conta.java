package com.milioli.gestaoContas.model.entity.conta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.milioli.gestaoContas.enums.SimNaoEnum;
import com.milioli.gestaoContas.enums.TipoContaEnum;
import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contas", schema = "gestao_contas")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Informe uma pessoa")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "i_pessoas", referencedColumnName = "id")
    private Pessoa pessoa;

    @NotNull(message = "Informe um saldo")
    @Column(name = "saldo")
    private BigDecimal saldo;

    @NotNull(message = "Informe um limite de saque diário")
    @Column(name = "limite_saque_diario")
    private BigDecimal limiteSaqueDiario;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Informe se a conta está ativa")
    @Column(name = "ativo")
    private SimNaoEnum ativo;

    @Enumerated(value = EnumType.ORDINAL)
    @NotNull(message = "Informe o tipo de conta")
    @Column(name = "tipo_conta")
    private TipoContaEnum tipoConta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Informe a data de criação")
    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

}
