package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String showCart(@PathVariable("id") int id, Model model) {
        model.addAttribute("cart", cartService.getCartById(id));
        return "/cart/show-cart";
    }

    @GetMapping()
    public String showCartForCustomer(Principal principal, Model model) {
        Cart cart = cartService.getCartByCustomerEmail(principal.getName());
        model.addAttribute("cart", cart);
        //model.addAttribute("books", bookService.getListOfBooksByCartId(cart.getId()));
        return "/cart/show-cart";
    }


}
