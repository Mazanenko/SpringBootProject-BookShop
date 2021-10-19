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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/{customerId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String showCart(@PathVariable("customerId") int customerId, Model model) {
        model.addAttribute("cart", cartService.getCartByCustomerId(customerId));
        return "/cart/show-cart";
    }

    @GetMapping()
    public String showCartForCustomer(@RequestParam(required = false) String error,
                                      Principal principal, ModelMap model) {
        Cart cart = cartService.getCartByCustomerEmail(principal.getName());
        model.addAttribute("cart", cart);
        model.addAttribute("error", error);
        return "/cart/show-cart";
    }

    //not complete
    @PostMapping("/{id}/add")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String addToCart(@ModelAttribute("order") Order order, @PathVariable("id") int id) {
        return "redirect:/books";
    }

    @PostMapping("/add-{bookId}")
    public String addToCartForCustomer(@PathVariable("bookId") int bookId, Principal principal) {

        if (customerService.authenticatedUserIsCustomer()) {
            Customer customer = customerService.getCustomerByEmail(principal.getName());
            int cartId = customer.getCart().getId();
            Order order = new Order(cartId, bookId, 1);

            if (bookService.isBookAvailable(order.getProductId())) {
                try {
                    cartService.addOrderToCart(order);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return "redirect:/cart";
            }
        }
        return "redirect:/books";
    }

    @PatchMapping("increment-{bookId}")
    public String incrementProduct(@PathVariable("bookId") int bookId, Principal principal,
                                   RedirectAttributes attributes) {
        if (customerService.authenticatedUserIsCustomer()) {
            Cart cart = cartService.getCartByCustomerEmail(principal.getName());
            try {
                cartService.incrementProduct(bookId, cart);
            } catch (SQLException e) {
                // not good enough
                attributes.addAttribute("error", "No more available books");
            }
        }
        return "redirect:/cart";
    }

    @PatchMapping("decrement-{bookId}")
    public String decrementProduct(@PathVariable("bookId") int bookId, Principal principal) {
        if (customerService.authenticatedUserIsCustomer()) {
            Cart cart = cartService.getCartByCustomerEmail(principal.getName());
            cartService.decrementProduct(bookId, cart);
        }
        return "redirect:/cart";
    }

    @DeleteMapping("/delete-{bookId}")
    @Secured("ROLE_CUSTOMER")
    public String deleteFromCartForCustomer(@PathVariable("bookId") int bookId, Principal principal) {
            Cart cart = cartService.getCartByCustomerEmail(principal.getName());
            cartService.deleteOrderFromCart(bookId, cart);
        return "redirect:/cart";
    }

    @DeleteMapping("/{customerId}/delete-{bookId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String deleteFromCartForManager(@PathVariable("customerId") int customerId,
                                           @PathVariable("bookId") int bookId) {

        Cart cart = cartService.getCartByCustomerId(customerId);
        cartService.deleteOrderFromCart(bookId, cart);
        return "redirect:/cart/{customerId}";
    }

}
