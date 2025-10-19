package com.example.JCB_mgnmtApp.service;

import com.example.JCB_mgnmtApp.dto.BookingDTO;
import com.example.JCB_mgnmtApp.model.*;
import com.example.JCB_mgnmtApp.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final ProjectService projectService;
    private final MachineService machineService;
    private final OperatorService operatorService;

    public BookingService(BookingRepository bookingRepo,
                          ProjectService projectService,
                          MachineService machineService,
                          OperatorService operatorService) {
        this.bookingRepo = bookingRepo;
        this.projectService = projectService;
        this.machineService = machineService;
        this.operatorService = operatorService;
    }

    public List<Booking> getAll() { return bookingRepo.findAll(); }
    public Booking getBookingById(Long id) { return bookingRepo.findById(id).orElseThrow(); }
    public void deleteById(Long id) { bookingRepo.deleteById(id); }

    @Transactional
    public Booking create(BookingDTO dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        LocalDate today = LocalDate.now();
        if (dto.getStartDate().isBefore(today) || dto.getEndDate().isBefore(today)) {
            throw new IllegalArgumentException("Dates cannot be in the past");
        }

        Machine machine = machineService.getById(dto.getMachineId());

        boolean hasOverlap = !bookingRepo
                .findOverlaps(machine, dto.getStartDate(), dto.getEndDate()).isEmpty();
        if (hasOverlap) {
            throw new IllegalStateException("Machine is already booked for the selected dates");
        }

        Project project = projectService.getById(dto.getProjectId());
        Booking booking = new Booking(project, machine, dto.getStartDate(), dto.getEndDate());
        // confirmed defaults to false
        return bookingRepo.save(booking);
    }

    @Transactional
    public Booking confirm(Long bookingId, boolean confirmed) {
        Booking b = getBookingById(bookingId);
        b.setConfirmed(confirmed);
        return bookingRepo.save(b);
    }

    @Transactional
    public Booking assignOperator(Long bookingId, Long operatorId) {
        Booking booking = getBookingById(bookingId);
        Operator op = operatorService.getById(operatorId);
        booking.setOperator(op);
        return bookingRepo.save(booking);
    }

    /** User-initiated delete: block if confirmed */
    public void deleteByUser(Long id) {
        Booking b = getBookingById(id);
        if (b.isConfirmed()) {
            throw new IllegalStateException("This booking is confirmed by admin and cannot be deleted.");
        }
        bookingRepo.deleteById(id);
    }

    @Transactional
    public void updateBookingByAdmin(Long id, Long operatorId, boolean confirmed) {
        Booking booking = getBookingById(id);
        Operator operator = operatorService.getById(operatorId);
        booking.setOperator(operator);
        booking.setConfirmed(confirmed);
        bookingRepo.save(booking);
    }

}
