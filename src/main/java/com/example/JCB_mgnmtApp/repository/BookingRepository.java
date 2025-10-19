package com.example.JCB_mgnmtApp.repository;

import com.example.JCB_mgnmtApp.model.Booking;
import com.example.JCB_mgnmtApp.model.Machine;
import com.example.JCB_mgnmtApp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Overlap check for a machine within date range
    @Query("""
           select b from Booking b
           where b.machine = :machine
             and (b.startDate <= :endDate and b.endDate >= :startDate)
           """)
    List<Booking> findOverlaps(Machine machine, LocalDate startDate, LocalDate endDate);

    List<Booking> findByProject(Project project);
}
