package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Manager;
import com.mazanenko.petproject.firstspringcrudapp.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public String showAllManagers(Model model) {
        model.addAttribute("managers", managerService.getAllManagers());
        return "/people/managers/managers-list";
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String showManager(@PathVariable("id") int id, Model model) {
        model.addAttribute("manager", managerService.getManagerById(id));
        return "/people/managers/show-manager";
    }

    @GetMapping("/profile")
    public String showProfileForManager(Principal principal, Model model) {
        model.addAttribute("manager", managerService.getManagerByEmail(principal.getName()));
        return "/people/managers/show-manager";
    }


    @GetMapping("/new")
    public String newManager(Model model) {
        model.addAttribute("manager", new Manager());
        return "/people/managers/new-manager";
    }

    @PostMapping
    public String createManager(@ModelAttribute("manager") @Valid Manager manager, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/people/managers/new-manager";
        } else {
            try {
                managerService.createManager(manager);
            } catch (DataAccessException e) {
                if (e.getCause().getMessage().contains("email_unique")) {
                    bindingResult.addError(new FieldError("manager", "email"
                            , manager.getEmail() + " already registered! Please, input new one."));
                }
                return "/people/managers/new-manager";
            }
        }
        return "redirect:/manager";
    }


    @GetMapping("/{id}/edit")
    public String editManager(@PathVariable("id") int id, Model model) {
        model.addAttribute("manager", managerService.getManagerById(id));
        return "/people/managers/edit-manager";
    }

    @PatchMapping("/{id}")
    public String updateManager(@ModelAttribute("manager") @Valid Manager manager, BindingResult bindingResult
            , @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/people/managers/edit-manager";
        } else {
            try {
                managerService.updateManagerById(id, manager);
            } catch (DataAccessException e) {

                if (e.getCause().getMessage().contains("email_unique")) {
                    bindingResult.addError(new FieldError("manager", "email"
                            , manager.getEmail() + " already registered! Please, input new one."));
                }
                return "/people/managers/edit-manager";
            }
        }
        return "redirect:/manager";
    }

    @DeleteMapping("/{id}")
    public String deleteManager(@PathVariable("id") int id) {
        managerService.deleteManagerById(id);
        return "redirect:/manager";
    }
}
