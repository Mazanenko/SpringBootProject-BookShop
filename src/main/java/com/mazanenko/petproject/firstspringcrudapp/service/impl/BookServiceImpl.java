package com.mazanenko.petproject.firstspringcrudapp.service.impl;

import com.mazanenko.petproject.firstspringcrudapp.dao.BookDAO;
import com.mazanenko.petproject.firstspringcrudapp.entity.Book;
import com.mazanenko.petproject.firstspringcrudapp.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.firstspringcrudapp.service.BookService;
import com.mazanenko.petproject.firstspringcrudapp.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookDAO bookDAO;
    private final SubscriptionService subscriptionService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BookServiceImpl(BookDAO bookDAO, SubscriptionService subscriptionService,
                           ApplicationEventPublisher applicationEventPublisher) {

        this.bookDAO = bookDAO;
        this.subscriptionService = subscriptionService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createBook(Book book) {
        bookDAO.create(book);
    }

    @Override
    public Book getBookById(int id) {
        Book book = bookDAO.read(id);
        book.setSubscribersList(subscriptionService.listOfSubscribers(id));
        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.readAll().stream().sorted(Comparator.comparing(Book::getName)).collect(Collectors.toList());
    }

    @Override
    public void updateBookById(int id, Book updatedBook) {
        if (bookDAO.read(id).getAvailableQuantity() == 0 && updatedBook.getAvailableQuantity() > 0) {
            publishArrivalEvent(id);
        }
        bookDAO.update(id, updatedBook);
    }

    @Override
    public void setQuantity(int bookId, int newQuantity) {
        if (bookDAO.read(bookId).getAvailableQuantity() == 0 && newQuantity > 0) {
            publishArrivalEvent(bookId);
        }
        bookDAO.updateQuantity(bookId, newQuantity);
    }

    @Override
    public void incrementBookQuantity(int bookId) {
        Book book = getBookById(bookId);
        if (book != null) {
            int quantity = book.getAvailableQuantity();
            book.setAvailableQuantity(++quantity);
            updateBookById(bookId, book);
        }
    }

    @Override
    public void decrementBookQuantity(int bookId) throws SQLException {
        Book book = getBookById(bookId);
        if (book != null) {
            int quantity = book.getAvailableQuantity();

            if (quantity >= 1) {
                book.setAvailableQuantity(--quantity);
                updateBookById(bookId, book);
            } else throw new SQLException();
        }
    }

    @Override
    public void deleteBookById(int id) {
        bookDAO.delete(id);
    }

    @Override
    public boolean isBookAvailable(int bookId) {
        Book book = getBookById(bookId);
        return ((book != null) && (book.getAvailableQuantity() > 0));
    }


    private void publishArrivalEvent(int productId) {
        applicationEventPublisher.publishEvent(new ProductArrivalEvent(getBookById(productId)));
    }
}
