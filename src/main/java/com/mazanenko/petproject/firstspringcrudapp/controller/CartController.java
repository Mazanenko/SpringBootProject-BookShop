package com.mazanenko.petproject.firstspringcrudapp.controller;

import com.mazanenko.petproject.firstspringcrudapp.entity.Cart;
import com.mazanenko.petproject.firstspringcrudapp.service.BookService;
import com.mazanenko.petproject.firstspringcrudapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final BookService bookService;

    @Autowired
    public CartController(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    @GetMapping("/{customerId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String showCart(@PathVariable("customerId") int customerId, Model model) {
        model.addAttribute("cart", cartService.getCartByCustomerId(customerId));
        return "/cart/show-cart";
    }

    @GetMapping()
    @Secured("ROLE_CUSTOMER")
    public String showCartForCustomer(Principal principal, ModelMap model) {
        Cart cart = cartService.getCartByCustomerEmail(principal.getName());
        model.addAttribute("cart", cart);
        return "/cart/show-cart";
    }

    @GetMapping("/{cartId}-add-book")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String addBookToCustomer(@PathVariable("cartId") int cartId, ModelMap model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("cart", cartService.getCartById(cartId));
        return "/books/list";
    }

    @PostMapping("/{customerId}/add-{bookId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String addToCartForManager(@PathVariable("bookId") int bookId,
                                      @PathVariable("customerId") int customerId) {

        try {
            cartService.addToCartByCustomerId(customerId, bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("redirect:/cart/%s", customerId);
    }


    @PostMapping("/add-{bookId}")
    @Secured("ROLE_CUSTOMER")
    public String addToCartForCustomer(@PathVariable("bookId") int bookId, Principal principal) {

        if (bookService.isBookAvailable(bookId)) {
            try {
                cartService.addToCartByCustomerEmail(principal.getName(), bookId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "redirect:/cart";
        }
        return "redirect:/books";
    }

    @PatchMapping("/increment-{bookId}")
    @Secured("ROLE_CUSTOMER")
    public String incrementProductForCustomer(@PathVariable("bookId") int bookId, Principal principal,
                                              ModelMap modelMap) {

        Cart cart = cartService.getCartByCustomerEmail(principal.getName());

        try {
            cartService.incrementProduct(bookId, cart);
            return "redirect:/cart";
        } catch (SQLException e) {
            // not good enough. Need to change alert in view to sth else
            modelMap.addAttribute("cart", cart);
            modelMap.addAttribute("error", "No more available books");
            return "/cart/show-cart";
        }
    }

    @PatchMapping("/{customerId}/increment-{bookId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String incrementProductForManager(@PathVariable("bookId") int bookId,
                                             @PathVariable("customerId") int customerId, ModelMap modelMap) {

        Cart cart = cartService.getCartByCustomerId(customerId);

        try {
            cartService.incrementProduct(bookId, cart);
            return "redirect:/cart/{customerId}";
        } catch (SQLException e) {
            // not good enough. Need to change alert in view to sth else
            modelMap.addAttribute("cart", cart);
            modelMap.addAttribute("error", "No more available books");
            return "/cart/show-cart";
        }
    }

    @PatchMapping("/decrement-{bookId}")
    @Secured("ROLE_CUSTOMER")
    public String decrementProductForCustomer(@PathVariable("bookId") int bookId, Principal principal) {

        Cart cart = cartService.getCartByCustomerEmail(principal.getName());
        cartService.decrementProduct(bookId, cart);
        return "redirect:/cart";
    }

    @PatchMapping("/{customerId}/decrement-{bookId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public String decrementProductForManager(@PathVariable("bookId") int bookId,
                                             @PathVariable("customerId") int customerId) {

        Cart cart = cartService.getCartByCustomerId(customerId);
        cartService.decrementProduct(bookId, cart);
        return "redirect:/cart/{customerId}";
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
