package com.mazanenko.petproject.bookshop.controller;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.service.BookService;
import com.mazanenko.petproject.bookshop.service.CustomerService;
import com.mazanenko.petproject.bookshop.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final SubscriptionService subscriptionService;
    private final CustomerService customerService;

    @Autowired
    public BookController(BookService bookService, SubscriptionService subscriptionService, CustomerService customerService) {
        this.bookService = bookService;
        this.subscriptionService = subscriptionService;
        this.customerService = customerService;
    }

    @GetMapping()
    public String showAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/list";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, ModelMap model, Principal principal) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);

        if (customerService.authenticatedUserIsCustomer()) {
            model.addAttribute("subscribed", customerService.isSubscribedToArrival(principal, book));
        }
        return "books/show";
    }


    @GetMapping("/new-book")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new-book";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new-book";
        } else {
            bookService.createBook(book);
            return "redirect:/books";
        }
    }


    @GetMapping("/{id}/edit-book")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "books/edit-book";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit-book";
        } else {
            bookService.updateBookById(id, book);
            return "redirect:/books";
        }
    }


    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBookById(id);
        return "redirect:/books";
    }

    @PostMapping("/subscribe-{bookId}")
    public String subscribeToArrival(@PathVariable("bookId") int bookId, Principal principal) {
        subscriptionService.subscribeByCustomerEmail(bookId, principal.getName());
        return String.format("redirect:/books/%s", bookId);
    }

    @PostMapping("/unsubscribe-{bookId}")
    public String unsubscribeToArrival(@PathVariable("bookId") int bookId, Principal principal) {
        subscriptionService.unsubscribeByCustomerEmail(bookId, principal.getName());
        return String.format("redirect:/books/%s", bookId);
    }
}
