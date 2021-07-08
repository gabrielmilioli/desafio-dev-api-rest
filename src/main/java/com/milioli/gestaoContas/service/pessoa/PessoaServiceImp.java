package com.milioli.gestaoContas.service.pessoa;

import com.milioli.gestaoContas.model.entity.pessoa.Pessoa;
import com.milioli.gestaoContas.model.repository.pessoa.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaServiceImp implements PessoaService {

    @Autowired
    PessoaRepository repository;

    @Override
    public Pessoa getById(Long id) {
        return repository.getById(id);
    }

}
