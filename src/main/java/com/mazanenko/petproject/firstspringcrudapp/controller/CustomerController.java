package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.DeliveryAddress;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/people/customers")
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
    public String showCustomer(@PathVariable("id") int id, Model model) {
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
                                 BindingResult addressResult, HttpServletRequest httpServletRequest) {

        String tempPass = customer.getPassword();

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
            if (!(authenticated())) {
                authWithHttpServletRequest(httpServletRequest, customer.getEmail(), tempPass);
                return "redirect:/";
            }
            return "redirect:/people/customers";
        }
    }


    @GetMapping("/{id}/edit")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String editCustomer(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        model.addAttribute("address", customerService.getCustomerById(id).getDeliveryAddress());
        return "/people/customers/edit-customer";
    }

    @GetMapping("/profile/edit")
    public String editProfileForCustomer(Principal principal, ModelMap modelMap) {
        String email = principal.getName();
        modelMap.addAttribute("customer", customerService.getCustomerByEmail(email));
        modelMap.addAttribute("address", customerService.getCustomerByEmail(email).getDeliveryAddress());
        return "/people/customers/edit-customer";
    }

    @PatchMapping(value = {"/profile/{id}", "/profile/update"})
    public String updateCustomer(@ModelAttribute("customer") @Valid Customer customer,
                                 BindingResult customerResult,
                                 @ModelAttribute("address") @Valid DeliveryAddress address,
                                 BindingResult addressResult, @PathVariable(required = false) Integer id,
                                 Principal principal) {

        // needed for user can't be able to change other user profile by changing {id} in edit form
        if (id == null) {
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
            if (authenticatedUserIsCustomer()) {
                return "redirect:/people/customers/profile";
            } else return "redirect:/people/customers";
        }
    }


    @DeleteMapping(value = {"/profile/{id}", "/profile/delete"})
    public String deleteCustomer(@PathVariable(required = false) Integer id, Principal principal) {
        if (id == null) {
            customerService.deleteCustomerByEmail(principal.getName());
        } else {
            customerService.deleteCustomerById(id);
        }

        if (authenticatedUserIsCustomer()) {
            SecurityContextHolder.clearContext();
            return "redirect:/";
        } else {
            return "redirect:/people/customers";
        }
    }


    private boolean authenticatedUserIsCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER"));
    }

    private boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) && (authentication.isAuthenticated())
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("error while login" + e.getMessage());
            e.printStackTrace();
        }
    }
}
