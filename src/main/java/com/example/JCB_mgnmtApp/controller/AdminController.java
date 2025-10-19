package com.example.JCB_mgnmtApp.controller;

import com.example.JCB_mgnmtApp.model.*;
import com.example.JCB_mgnmtApp.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProjectService projectService;
    private final MachineService machineService;
    private final BookingService bookingService;
    private final OperatorService operatorService;

    public AdminController(ProjectService projectService,
                           MachineService machineService,
                           BookingService bookingService,
                           OperatorService operatorService) {
        this.projectService = projectService;
        this.machineService = machineService;
        this.bookingService = bookingService;
        this.operatorService = operatorService;
    }

    // ------------------ DASHBOARD ------------------
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // ------------------ PROJECTS ------------------
    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectService.listAll());
        return "admin/projects";
    }

    @GetMapping("/projects/new")
    public String newProject(Model model) {
        model.addAttribute("project", new Project());
        return "admin/project_form";
    }

    @PostMapping("/projects/save")
    public String saveProject(@ModelAttribute Project project) {
        projectService.save(project);
        return "redirect:/admin/projects";
    }

    @GetMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return "redirect:/admin/projects";
    }

    // ------------------ MACHINES ------------------
    @GetMapping("/machines")
    public String machines(Model model) {
        model.addAttribute("machines", machineService.findAll());
        return "admin/machines";
    }

    @GetMapping("/machines/new")
    public String newMachine(Model model) {
        model.addAttribute("machine", new Machine());
        return "admin/machine_form";
    }

    @PostMapping("/machines/save")
    public String saveMachine(@ModelAttribute Machine machine) {
        machineService.save(machine);
        return "redirect:/admin/machines";
    }

    @GetMapping("/machines/delete/{id}")
    public String deleteMachine(@PathVariable Long id) {
        machineService.safeDelete(id);
        return "redirect:/admin/machines";
    }

    // ------------------ OPERATORS ------------------
    @GetMapping("/operators")
    public String operators(Model model) {
        model.addAttribute("operators", operatorService.listAll());
        return "admin/operators";
    }

    @GetMapping("/operator_form")
    public String operatorForm(Model model) {
        model.addAttribute("operator", new Operator());
        return "admin/operator_form";
    }

    @PostMapping("/operators/save")
    public String saveOperator(@ModelAttribute Operator operator) {
        operatorService.save(operator);
        return "redirect:/admin/operators";
    }

    @GetMapping("/operator/{id}/delete")
    public String deleteOperator(@PathVariable Long id) {
        operatorService.delete(id);
        return "redirect:/admin/operators";
    }

    // ------------------ BOOKINGS ------------------
    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.getAll());
        model.addAttribute("operators", operatorService.listAll());
        return "admin/bookings";
    }

    @PostMapping("/bookings/{id}/confirm")
    public String confirmBooking(@PathVariable Long id,
                                 @RequestParam("operatorId") Long operatorId) {
        bookingService.assignOperator(id, operatorId);
        bookingService.confirm(id, true);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/bookings/{id}/edit")
    public String editBooking(@PathVariable Long id, Model model) {
        var booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        model.addAttribute("operators", operatorService.listAll());
        return "admin/edit_booking";
    }

    @PostMapping("/bookings/{id}/update")
    public String updateBooking(@PathVariable Long id,
                                @RequestParam("operatorId") Long operatorId,
                                @RequestParam(value = "confirmed", required = false) boolean confirmed) {
        bookingService.updateBookingByAdmin(id, operatorId, confirmed);
        return "redirect:/admin/bookings?updated=true";
    }
}
