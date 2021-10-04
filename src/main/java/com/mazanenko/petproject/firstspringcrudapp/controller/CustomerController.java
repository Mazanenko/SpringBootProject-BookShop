package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "/people/customers/customers-list";
    }

    @GetMapping("/{id}")
    public String showCustomer(@PathVariable("id") int id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "/people/customers/show-customer";
    }


    @GetMapping("/new")
    public String newCustomer(ModelMap model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("address", new DeliveryAddress());
        return "/people/customers/new-customer";
    }

    @PostMapping
    public String createCustomer(@ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult customerResult,
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult addressResult) {
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
            return "redirect:/people/customers";
        }
    }


    @GetMapping("/{id}/edit")
    public String editCustomer(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        model.addAttribute("address", customerService.getCustomerById(id).getDeliveryAddress());
        return "/people/customers/edit-customer";
    }

    @PatchMapping("/{id}")
    public String updateCustomer(@ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult customerResult,
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult addressResult, @PathVariable("id") int id) {

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
                return "/people/customers/new-customer";
            }
            return "redirect:/people/customers";
        }
    }


    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable("id") int id) {
        customerService.deleteCustomerById(id);
        return "redirect:/people/customers";
    }
}
