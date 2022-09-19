package com.mazanenko.petproject.bookshop.controller;

import com.mazanenko.petproject.bookshop.DTO.CartDto;
import com.mazanenko.petproject.bookshop.service.CartService;
import com.mazanenko.petproject.bookshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/{customerId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<CartDto> getCart(@PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomerId(customerId));
    }

    @GetMapping()
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<CartDto> getCartForCustomer(Principal principal) {
        return ResponseEntity.ok(cartService.getCartByCustomerEmail(principal.getName()));
    }

    @PostMapping("/{customerId}/addProduct/{productId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<CartDto> addProductToCartForManager(@PathVariable("productId") Long productId,
                                                              @PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(cartService.addToCartByCustomerId(customerId, productId));
    }


    @PostMapping("/addProduct/{productId}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<CartDto> addToCartForCustomer(@PathVariable("productId") Long productId,
                                                        Principal principal) {

        if (productService.isProductAvailable(productId)) {
            return ResponseEntity.ok(cartService.addToCartByCustomerEmail(principal.getName(), productId));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Product with id: %d does not available", productId));
    }

    @PatchMapping("/incrementOrderQuantity/{orderId}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<CartDto> incrementProductInOrderForCustomer(@PathVariable("orderId") Long orderId,
                                                                      Principal principal) {
        return ResponseEntity.ok(cartService.incrementProductInOrderByCustomerEmail(principal.getName(), orderId));
    }

    @PatchMapping("/{customerId}/incrementOrderQuantity/{orderId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<CartDto> incrementProductInOrderForManager(@PathVariable("orderId") Long orderId,
                                                                     @PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(cartService.incrementProductInOrderByCustomerId(customerId, orderId));
    }

    @PatchMapping("/decrementOrderQuantity/{orderId}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<CartDto> decrementProductInOrderForCustomer(@PathVariable("orderId") Long orderId,
                                                                      Principal principal) {
        return ResponseEntity.ok(cartService.decrementProductInOrderByCustomerEmail(principal.getName(), orderId));
    }

    @PatchMapping("/{customerId}/decrementOrderQuantity/{orderId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<CartDto> decrementProductInOrderForManager(@PathVariable("orderId") Long orderId,
                                                                     @PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(cartService.decrementProductInOrderByCustomerId(customerId, orderId));
    }

    @DeleteMapping("/delete/{orderId}")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<?> deleteFromCartForCustomer(@PathVariable("orderId") Long orderId,
                                                       Principal principal) {
        cartService.deleteOrderFromCartByCustomerEmail(orderId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{customerId}/delete/{orderId}")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<?> deleteFromCartForManager(@PathVariable("customerId") Long customerId,
                                                      @PathVariable("orderId") Long orderId) {
        cartService.deleteOrderFromCartByCustomerId(orderId, customerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/makeOrder")
    @Secured("ROLE_CUSTOMER")
    public ResponseEntity<CartDto> makeAnOrder(Principal principal) {
        return ResponseEntity.ok(cartService.makeAnOrderByCustomerEmail(principal.getName()));
    }
}
