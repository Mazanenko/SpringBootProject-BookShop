package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

    @GetMapping("{id}")
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
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/people/customers/new-customer";
        } else {
            customerService.createCustomer(customer, address);
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
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "/people/customers/edit-customer";
        } else {
            customerService.updateCustomerById(id, customer, address);
            return "redirect:/people/customers";
        }
    }


    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable("id") int id) {
        customerService.deleteCustomerById(id);
        return "redirect:/people/customers";
    }
}
