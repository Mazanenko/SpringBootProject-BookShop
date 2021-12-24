package com.mazanenko.petproject.bookshop.controller;

import com.mazanenko.petproject.bookshop.entity.Customer;
import com.mazanenko.petproject.bookshop.entity.DeliveryAddress;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "/people/customers/customers-list";
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String showCustomer(@PathVariable("id") Long id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "/people/customers/show-customer";
    }

    @GetMapping("/profile")
    public String showProfileForCustomer(Principal principal, Model model) {
        model.addAttribute("customer", customerService.getCustomerByEmail(principal.getName()));
        return "/people/customers/show-customer";
    }


    @GetMapping("/new")
    public String newCustomer(ModelMap model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("address", new DeliveryAddress());
        return "/people/customers/new-customer";
    }

    @PostMapping("/create")
    public String createCustomer(@ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult customerResult,
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult addressResult, Model model) {

        if (customerResult.hasErrors() || addressResult.hasErrors()) {
            return "/people/customers/new-customer";
        } else {
            try {
                customerService.createCustomer(customer, address);
            } catch (DataAccessException e) {

                if (e.getCause().getMessage().contains("unique_email")) {
                    customerResult.addError(new FieldError("customer", "email"
                            , customer.getEmail() + " already registered! Please, input new one."));
                }
                return "/people/customers/new-customer";
            }
            if (!customerService.isAuthenticated()) {
                model.addAttribute("message", "Please, check your email for message for confirm registration!");
                return "/people/customers/activate-customer";
            } else return "redirect:/customer";
        }
    }

    @GetMapping("/activate/{code}")
    public String accountActivation(Model model, @PathVariable("code") String code) {
        if (customerService.activateUser(code)) {
            model.addAttribute("message", "Account successfully activated!");
        } else {
            model.addAttribute("message", "Activation code not found.");
        }
        return "/people/customers/activate-customer";
    }

    @GetMapping("/{id}/edit")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String editCustomer(@PathVariable("id") Long id, ModelMap model) {
        Customer customer = customerService.getCustomerById(id);

        model.addAttribute("customer", customer);
        model.addAttribute("address", customer.getDeliveryAddress());
        return "/people/customers/edit-customer";
    }

    @GetMapping("/profile/edit")
    public String editProfileForCustomer(Principal principal, ModelMap modelMap) {
        Customer customer = customerService.getCustomerByEmail(principal.getName());

        modelMap.addAttribute("customer", customer);
        modelMap.addAttribute("address", customer.getDeliveryAddress());
        return "/people/customers/edit-customer";
    }

    @PatchMapping(value = {"/profile/{id}", "/profile/update"})
    public String updateCustomer(@ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult customerResult,
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult addressResult, @PathVariable(required = false) Long id,
                                 Principal principal) {

        // needed for user can't be able to change other user profile by changing {id} in edit form
        if (customerService.authenticatedUserIsCustomer()) {
            id = customerService.getCustomerByEmail(principal.getName()).getId();
        }

        if (customerResult.hasErrors() || addressResult.hasErrors()) {
            return "/people/customers/edit-customer";
        } else {
            try {
                customerService.updateCustomerById(id, customer, address);
            } catch (DataAccessException e) {

                if (e.getCause().getMessage().contains("unique_email")) {

                    customerResult.addError(new FieldError("customer", "email"
                            , customer.getEmail() + " already registered! Please, input new one."));
                }
                return "/people/customers/edit-customer";
            }
            // if true return customer profile page for customers
            if (customerService.authenticatedUserIsCustomer()) {
                return "redirect:/customer/profile";
            } else return "redirect:/customer";
        }
    }


    @DeleteMapping(value = {"/profile/{id}", "/profile/delete"})
    public String deleteCustomer(@PathVariable(required = false) Long id, Principal principal) {
        if (id == null) {
            customerService.deleteCustomerByEmail(principal.getName());
        } else {
            customerService.deleteCustomerById(id);
        }

        if (customerService.authenticatedUserIsCustomer()) {
            SecurityContextHolder.clearContext();
            return "redirect:/";
        } else {
            return "redirect:/customer";
        }
    }
}
