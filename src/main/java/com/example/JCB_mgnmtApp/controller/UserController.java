package com.example.JCB_mgnmtApp.controller;

import com.example.JCB_mgnmtApp.dto.BookingDTO;
import com.example.JCB_mgnmtApp.model.Booking;
import com.example.JCB_mgnmtApp.service.BookingService;
import com.example.JCB_mgnmtApp.service.MachineService;
import com.example.JCB_mgnmtApp.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final ProjectService projectService;
    private final MachineService machineService;
    private final BookingService bookingService;

    public UserController(ProjectService projectService,
                          MachineService machineService,
                          BookingService bookingService) {
        this.projectService = projectService;
        this.machineService = machineService;
        this.bookingService = bookingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.listAll());
        model.addAttribute("machines", machineService.listAll());
        model.addAttribute("bookings", bookingService.getAll());
        model.addAttribute("bookingDTO", new BookingDTO());
        return "user/dashboard";
    }

    @PostMapping("/book")
    public String book(@Valid @ModelAttribute("bookingDTO") BookingDTO dto,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.listAll());
            model.addAttribute("machines", machineService.listAll());
            model.addAttribute("bookings", bookingService.getAll());
            return "user/dashboard";
        }
        try {
            bookingService.create(dto);
            return "redirect:/user/dashboard?success=created";
        } catch (RuntimeException ex) {
            model.addAttribute("projects", projectService.listAll());
            model.addAttribute("machines", machineService.listAll());
            model.addAttribute("bookings", bookingService.getAll());
            model.addAttribute("error", ex.getMessage());
            return "user/dashboard";
        }
    }

    @PostMapping("/booking/{id}/delete")
    public String deleteByUser(@PathVariable Long id, Model model) {
        try {
            bookingService.deleteByUser(id);
            return "redirect:/user/dashboard?success=deleted";
        } catch (RuntimeException ex) {
            model.addAttribute("projects", projectService.listAll());
            model.addAttribute("machines", machineService.listAll());
            model.addAttribute("bookings", bookingService.getAll());
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("bookingDTO", new com.example.JCB_mgnmtApp.dto.BookingDTO());
            return "user/dashboard";
        }
    }
}
