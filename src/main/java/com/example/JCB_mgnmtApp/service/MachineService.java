package com.example.JCB_mgnmtApp.service;

import com.example.JCB_mgnmtApp.model.Machine;
import com.example.JCB_mgnmtApp.repository.MachineRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository repo;

    public MachineService(MachineRepository repo) {
        this.repo = repo;
    }

    // ✅ Method used by AdminController
    public List<Machine> findAll() {
        return repo.findAll();
    }

    // ✅ Method used by UserController
    public List<Machine> listAll() {
        return repo.findAll();
    }

    public Machine getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found"));
    }

    public Machine save(Machine m) {
        return repo.save(m);
    }

    public void safeDelete(Long id) {
        Machine m = repo.findById(id).orElseThrow(() -> new RuntimeException("Machine not found"));
        try {
            m.setAvailability(false);
            repo.save(m);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete machine — may be linked to existing bookings.");
        }
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
