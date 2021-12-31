package com.mazanenko.petproject.bookshop.service.impl;

import com.mazanenko.petproject.bookshop.annotation.LogException;
import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.entity.event.ProductArrivalEvent;
import com.mazanenko.petproject.bookshop.repository.BookRepository;
import com.mazanenko.petproject.bookshop.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

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

        LOGGER.info("The book {} was created", book);
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

        Book originalBook = getBookById(bookId);
        if (originalBook == null) {
            return;
        }

        if (originalBook.getAvailableQuantity() == 0 && updatedBook.getAvailableQuantity() > 0) {
            publishArrivalEvent(bookId);
        }
        updatedBook.setId(bookId);
        bookRepository.save(updatedBook);

        LOGGER.info("The book {} by {} with ID {} wos updated. Now it is {}",
                originalBook.getName(), originalBook.getAuthor(), bookId, updatedBook);
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

        LOGGER.info("New available quantity for the book {} by {} with ID {} is {}",
                book.getName(), book.getAuthor(), bookId, newQuantity);
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

        LOGGER.info("New available quantity for the book {} by {} with ID {} is {}",
                book.getName(), book.getAuthor(), bookId, book.getAvailableQuantity());
    }

    @Override
    @LogException
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

            LOGGER.info("New available quantity for the book {} by {} with ID {} is {}",
                    book.getName(), book.getAuthor(), bookId, book.getAvailableQuantity());
        } else throw new SQLException();
    }

    @Override
    public void deleteBookById(Long bookId) {
        if (bookId <= 0) {
            return;
        }
        bookRepository.deleteById(bookId);

        LOGGER.info("The book with ID {} was deleted", bookId);
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
