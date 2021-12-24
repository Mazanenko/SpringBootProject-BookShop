package com.mazanenko.petproject.bookshop.controller;

import com.mazanenko.petproject.bookshop.entity.Manager;
import com.mazanenko.petproject.bookshop.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String showManager(@PathVariable("id") Long id, Model model) {
        model.addAttribute("manager", managerService.getManagerById(id));
        return "/people/managers/show-manager";
    }

    @GetMapping("/profile")
    public String showProfileForManager(Principal principal, Model model) {
        model.addAttribute("manager", managerService.getManagerByEmail(principal.getName()));
        return "/people/managers/show-manager";
    }


    @GetMapping("/new")
    @Secured("ROLE_ADMIN")
    public String newManager(Model model) {
        model.addAttribute("manager", new Manager());
        return "/people/managers/new-manager";
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public String createManager(@ModelAttribute("manager") @Valid Manager manager, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("password")) {
            return "/people/managers/new-manager";
        } else {
            try {
                String credentials = managerService.createManager(manager);
                model.addAttribute("message", credentials);
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "/people/managers/new-manager-status";
            }
        }
        return "/people/managers/new-manager-status";
    }


    @GetMapping("/{id}/edit")
    public String editManager(@PathVariable("id") Long id, Model model) {
        model.addAttribute("manager", managerService.getManagerById(id));
        return "/people/managers/edit-manager";
    }

    @PatchMapping("/{id}")
    public String updateManager(@ModelAttribute("manager") @Valid Manager manager, BindingResult bindingResult
            , @PathVariable("id") Long id, Model model) {

        if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("email")) {
            System.out.println(bindingResult.getAllErrors());
            return "/people/managers/edit-manager";
        } else {
            try {
                manager.setEmail(managerService.getManagerById(id).getEmail());
                managerService.updateManagerById(id, manager);
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "/people/managers/new-manager-status";
            }
        }
        return "redirect:/manager";
    }

    @DeleteMapping("/{id}")
    public String deleteManager(@PathVariable("id") Long id, Model model) {
        try {
            managerService.deleteManagerById(id);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "/people/managers/new-manager-status";
        }
        return "redirect:/manager";
    }
}
