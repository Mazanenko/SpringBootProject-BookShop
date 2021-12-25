package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.bookshop.repository.BookRepository;
import com.mazanenko.petproject.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           ApplicationEventPublisher applicationEventPublisher) {

        this.bookRepository = bookRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createBook(Book book) {
        if (book == null) {
            return;
        }
        bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long bookId) {
        if (bookId == null) {
            return null;
        }
        return bookRepository.findById(bookId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    @Transactional
    public void updateBookById(Long bookId, Book updatedBook) {
        if (bookId <= 0 || updatedBook == null) {
            return;
        }

        if (getBookById(bookId).getAvailableQuantity() == 0 && updatedBook.getAvailableQuantity() > 0) {
            publishArrivalEvent(bookId);
        }
        updatedBook.setId(bookId);
        bookRepository.save(updatedBook);
    }

    @Override
    public void setQuantity(Long bookId, int newQuantity) {
        if (bookId <= 0 || newQuantity < 0) {
            return;
        }

        Book book = getBookById(bookId);
        if (book == null) {
            return;
        }

        if (book.getAvailableQuantity() == 0 && newQuantity > 0) {
            publishArrivalEvent(bookId);
        }
        book.setAvailableQuantity(newQuantity);
        bookRepository.save(book);
    }

    @Override
    public void incrementBookQuantity(Long bookId) {
        if (bookId <= 0) {
            return;
        }

        Book book = getBookById(bookId);
        if (book == null) {
            return;
        }

        int quantity = book.getAvailableQuantity();
        book.setAvailableQuantity(++quantity);
        updateBookById(bookId, book);
    }

    @Override
    public void decrementBookQuantity(Long bookId) throws SQLException {
        if (bookId <= 0) {
            return;
        }

        Book book = getBookById(bookId);
        if (book == null) {
            return;
        }

        int quantity = book.getAvailableQuantity();

        if (quantity >= 1) {
            book.setAvailableQuantity(--quantity);
            updateBookById(bookId, book);
        } else throw new SQLException();
    }

    @Override
    public void deleteBookById(Long bookId) {
        if (bookId <= 0) {
            return;
        }
        bookRepository.deleteById(bookId);
    }

    @Override
    public boolean isBookAvailable(Long bookId) {
        if (bookId <= 0) {
            return false;
        }

        Book book = getBookById(bookId);
        return ((book != null) && (book.getAvailableQuantity() > 0));
    }


    private void publishArrivalEvent(Long bookId) {
        if (bookId <= 0) {
            return;
        }
        applicationEventPublisher.publishEvent(new ProductArrivalEvent(getBookById(bookId)));
    }
}
