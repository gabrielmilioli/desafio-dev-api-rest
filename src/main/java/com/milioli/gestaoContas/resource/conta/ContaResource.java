package com.milioli.gestaoContas.resource.conta;

import com.milioli.gestaoContas.model.entity.conta.Conta;
import com.milioli.gestaoContas.service.conta.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.milioli.gestaoContas.exception.RegraNegocioException.extractMessageFromException;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaResource {

    private final ContaService service;

    @PostMapping
    public ResponseEntity criar(@RequestBody ContaDto dto) {
        try {
            final Conta conta = ContaDto.toEntity(dto);
            final ContaDto contaDto = ContaDto.toDto(service.criar(conta));

            return new ResponseEntity(contaDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(extractMessageFromException(e));
        }
    }

    @PutMapping("{id}/depositar")
    public ResponseEntity depositar(@PathVariable("id") Long id,
                                    @RequestBody BigDecimal valor) {
        try {
            final Conta conta = service.getById(id);

            service.depositar(conta, valor);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(extractMessageFromException(e));
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity getSaldoById(@PathVariable("id") Long id) {
        try {
            final Conta conta = service.getById(id);
            return new ResponseEntity(conta.getSaldo(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(extractMessageFromException(e));
        }
    }

    @PutMapping("{id}/sacar")
    public ResponseEntity sacar(@PathVariable("id") Long id,
                                @RequestBody BigDecimal valor) {
        try {
            final Conta conta = service.getById(id);

            service.sacar(conta, valor);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(extractMessageFromException(e));
        }
    }

    @PutMapping("{id}/bloquear")
    public ResponseEntity bloquear(@PathVariable("id") Long id) {
        try {
            final Conta conta = service.getById(id);

            service.bloquear(conta);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(extractMessageFromException(e));
        }
    }

    @GetMapping("{id}/transacoes")
    public ResponseEntity getTransacoesFromConta(@PathVariable("id") Long id,
                                                 @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                 @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        try {
            final Conta conta = service.getById(id);
            return new ResponseEntity(service.transacoes(conta, from, to), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(extractMessageFromException(e));
        }
    }

}
