package com.example.JCB_mgnmtApp.service;

import com.example.JCB_mgnmtApp.model.Operator;
import com.example.JCB_mgnmtApp.repository.OperatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {
    private final OperatorRepository repo;

    public OperatorService(OperatorRepository repo) { this.repo = repo; }

    public List<Operator> listAll() { return repo.findAll(); }
    public Operator getById(Long id) { return repo.findById(id).orElseThrow(); }

    public Operator save(Operator operator) { return repo.save(operator); }

    public void delete(Long id) { repo.deleteById(id); }
    public List<Operator> findAll() {
        return repo.findAll();
    }


}
