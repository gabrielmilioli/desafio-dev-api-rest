package com.milioli.gestaoContas.model.entity.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pessoas", schema = "gestao_contas")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Informe um nome")
    @Length(max = 200)
    @Column(name = "nome")
    private String nome;

    @NotEmpty(message = "Informe um CPF")
    @CPF(message = "Informe um CPF válido")
    @Column(name = "cpf")
    private String cpf;

    @NotNull(message = "Informe uma data de nascimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

}
