package com.milioli.gestaoContas.model.entity.transacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.milioli.gestaoContas.model.entity.conta.Conta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacoes", schema = "gestao_contas")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Informe uma conta")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "i_contas", referencedColumnName = "id")
    private Conta conta;

    @NotNull(message = "Informe um valor")
    @Column(name = "valor")
    private BigDecimal valor;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Informe a data de transação")
    @Column(name = "data_transacao")
    private LocalDate dataTransacao;

}
