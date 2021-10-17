package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.entity.Customer;
import com.mazanenko.petproject.firstspringcrudapp.entity.Order;
import com.mazanenko.petproject.firstspringcrudapp.service.BookService;
import com.mazanenko.petproject.firstspringcrudapp.service.CartService;
import com.mazanenko.petproject.firstspringcrudapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final BookService bookService;
    private final CustomerService customerService;

    @Autowired
    public CartController(CartService cartService, BookService bookService
            , CustomerService customerService) {
        this.cartService = cartService;
        this.bookService = bookService;
        this.customerService = customerService;
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
        return "/cart/show-cart";
    }

    @PostMapping("/{id}/add")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String addToCart(@ModelAttribute("order") Order order, @PathVariable("id") int id) {
        return "redirect:/books";
    }

    @PostMapping("/add-{bookId}")
    public String addToCartForCustomer(@PathVariable("bookId") int bookId, Principal principal) {

        Customer customer = customerService.getCustomerByEmail(principal.getName());
        int cartId = customer.getCart().getId();
        Order order = new Order(cartId, bookId, 1);

        if (bookService.isBookAvailable(order.getProductId())) {
            //order.setCartId(cartId);
            try {
                cartService.addOrderToCartById(order);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "redirect:/cart";
        }
        return "redirect:/books";
    }

}
