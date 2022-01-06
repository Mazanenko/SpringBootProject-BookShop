package com.mazanenko.petproject.bookshop.service;

import com.mazanenko.petproject.bookshop.entity.Book;
import com.mazanenko.petproject.bookshop.repository.BookRepository;
import com.mazanenko.petproject.bookshop.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService = Mockito.spy(new BookServiceImpl());

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private final Book BOOK = new Book();
    private final Long ID = 1L;
    private int availableQuantity = 3;


    @BeforeEach
    void setUp() {
        BOOK.setId(ID);
        BOOK.setName("Book name");
        BOOK.setAuthor("book author");
        BOOK.setGenre("book genre");
        BOOK.setPhotoURL("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f0/Sea-wolf_cover.jpg/" +
                "274px-Sea-wolf_cover.jpg");
        BOOK.setDescription("description of the book");
        BOOK.setAvailableQuantity(availableQuantity);
        BOOK.setPrice(5);
    }

    @Test
    void createBookShouldCreateNewBook() {
        // Given book
        //When
        bookService.createBook(BOOK);
        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(BOOK);
    }

    @Test
    void createBookWhenBookIsNull() {
        // Given book is null
        //When
        bookService.createBook(null);
        //Then
        Mockito.verifyNoInteractions(bookRepository);
    }

    @Test
    void getBookByIdShouldReturnBook() {
        //Given ID
        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        //Then
        Assertions.assertEquals(BOOK, bookService.getBookById(ID));
    }

    @Test
    void getBookByIdShouldReturnNullWhenIdIsNull() {
        //Given id is null
        //Then
        Assertions.assertNull(bookService.getBookById(null));
    }

    @Test
    void getAllBooksShouldReturnSortedListOfBooks() {
        //Given
        Book book2 = new Book(2L, "Second book", "description",
                1, 1, "Author", "genre", "URL");

        List<Book> books = new ArrayList<>();
        books.add(BOOK);
        books.add(book2);
        List<Book> sortedList = books.stream()
                .sorted(Comparator.comparing(Book::getName)).collect(Collectors.toList());

        //When
        Mockito.when(bookRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(sortedList);

        //Then
        Assertions.assertEquals(sortedList, bookService.getAllBooks());
    }

    @Test
    void updateBookByIdShouldUpdateBookWhenOriginalBookQuantityMoreThanZero() {
        //Given BOOK as originalBook
        Book updatedBook = new Book(2L, "Second book", "description",
                1, 1, "Author", "genre", "URL");

        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        bookService.updateBookById(ID, updatedBook);

        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(updatedBook);
        Mockito.verifyNoInteractions(applicationEventPublisher);
        Assertions.assertEquals(ID, updatedBook.getId());
    }

    @Test
    void updateBookByIdShouldUpdateBookWhenOriginalBookQuantityIsZero() {
        //Given BOOK as originalBook
        BOOK.setAvailableQuantity(0);
        Book updatedBook = new Book(2L, "Second book", "description",
                1, 1, "Author", "genre", "URL");

        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        bookService.updateBookById(ID, updatedBook);
        ArgumentCaptor<ApplicationEventPublisher> applicationEventPublisherCaptor = ArgumentCaptor
                .forClass(ApplicationEventPublisher.class);

        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(updatedBook);
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(applicationEventPublisherCaptor.capture());
        Assertions.assertEquals(ID, updatedBook.getId());
    }

    @Test
    void updateBookByIdWhenIdLessThanOneOrUpdatedBookIsNull() {
        //Given id = 0 and updatedBook is null
        //When
        bookService.updateBookById(0L, null);

        //Then
        Mockito.verifyNoInteractions(bookRepository);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void setQuantityShouldSetNewAvailableQuantityWhenCurrentQuantityMoreThanZero() {
        //Given
        int newQuantity = 3;

        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        bookService.setQuantity(ID, newQuantity);

        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(BOOK);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void setQuantityShouldSetNewAvailableQuantityWhenCurrentQuantityIsZero() {
        //Given
        BOOK.setAvailableQuantity(0);
        int newQuantity = 3;

        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        bookService.setQuantity(ID, newQuantity);
        ArgumentCaptor<ApplicationEventPublisher> applicationEventPublisherCaptor = ArgumentCaptor
                .forClass(ApplicationEventPublisher.class);

        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(BOOK);
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(applicationEventPublisherCaptor.capture());
    }

    @Test
    void setQuantityWhenIdLessThanOneOrNewQuantityLessThanZero() {
        //Given id = 0 and newQuantity = -1
        //When
        bookService.setQuantity(0L, -1);

        //Then
        Mockito.verifyNoInteractions(bookRepository);
        Mockito.verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void incrementBookQuantityShouldIncrementQuantity() {
        //Given book
        //When
        bookService.incrementBookQuantity(BOOK);

        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(BOOK);
        Assertions.assertEquals(++availableQuantity, BOOK.getAvailableQuantity());
    }

    @Test
    void incrementBookQuantityWhenBookIsNull() {
        //Given book is null
        //When
        bookService.incrementBookQuantity(null);

        //Then
        Mockito.verifyNoInteractions(bookRepository);
    }

    @Test
    void incrementBookQuantityWhenBookIdLessThanOne() {
        //Given book id = 0
        BOOK.setId(0L);
        //When
        bookService.incrementBookQuantity(BOOK);

        //Then
        Mockito.verifyNoInteractions(bookRepository);
    }

    @Test
    void decrementBookQuantityShouldDecrementQuantity() throws SQLException {
        //Given book
        //When
        bookService.decrementBookQuantity(BOOK);

        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).save(BOOK);
        Assertions.assertEquals(--availableQuantity, BOOK.getAvailableQuantity());
    }

    @Test
    void decrementBookQuantityWhenBookIsNull() throws SQLException {
        //Given book is null
        //When
        bookService.decrementBookQuantity(null);

        //Then
        Mockito.verifyNoInteractions(bookRepository);
    }

    @Test
    void decrementBookQuantityWhenBookIdLessThanOne() throws SQLException {
        //Given book id = 0
        BOOK.setId(0L);
        //When
        bookService.decrementBookQuantity(BOOK);

        //Then
        Mockito.verifyNoInteractions(bookRepository);
    }

    @Test
    void decrementBookQuantityShouldThrowExceptionWhenBookQuantityLessThanOne() {
        //Given book id = 0
        BOOK.setAvailableQuantity(0);
        String exceptionMessage = "Can't decrement quantity, because available quantity is less than one";

        //When
        Exception exception = Assertions.assertThrows(SQLException.class, () -> bookService.decrementBookQuantity(BOOK));

        //Then
        Mockito.verifyNoInteractions(bookRepository);
        Assertions.assertEquals(exceptionMessage, exception.getMessage());
        Assertions.assertEquals(0, BOOK.getAvailableQuantity());
    }

    @Test
    void deleteBookByIdShouldDeleteBook() {
        //Given book id
        //When
        bookService.deleteBookById(ID);
        //Then
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(ID);
    }

    @Test
    void deleteBookByIdWhenIdLessThanOne() {
        //Given book id = 0
        //When
        bookService.deleteBookById(0L);
        //Then
        Mockito.verifyNoInteractions(bookRepository);
    }

    @Test
    void isBookAvailableShouldReturnTrueWhenBookIsAvailable() {
        //Given book id
        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));

        //Then
        Assertions.assertTrue(bookService.isBookAvailable(ID));
    }

    @Test
    void isBookAvailableShouldReturnFalseWhenBookIdLessThanOne() {
        //Given book id = 0
        //Then
        Assertions.assertFalse(bookService.isBookAvailable(0L));
    }

    @Test
    void isBookAvailableShouldReturnFalseWhenBookIsNull() {
        //Given book is null
        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        //Then
        Assertions.assertFalse(bookService.isBookAvailable(ID));
    }

    @Test
    void isBookAvailableShouldReturnFalseWhenAvailableQuantityLessThanOne() {
        //Given
        BOOK.setAvailableQuantity(0);

        //When
        Mockito.when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));

        //Then
        Assertions.assertFalse(bookService.isBookAvailable(ID));
    }
}