package service;

import org.example.BookStore.exceptions.BookNotFoundException;
import org.example.BookStore.exceptions.OrderNotFoundException;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;
import org.example.BookStore.model.OrderStatus;
import org.example.BookStore.service.BookStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookStoreServiceTest {
    private BookStoreService bookStoreService;

    @BeforeEach
    void setUp() {
        bookStoreService = new BookStoreService();
    }

    @Test
    public void openOrderWithBooks() {
        List<Book> books = List.of(new Book(1, "crime and punishment", 20));
        Order order = bookStoreService.open(books, 20);
        assertNotNull(order, "Order should not be null");
        assertEquals(1, order.getBooks().size(), "Order should contain one book");
        assertEquals(20, order.getTotalPrice(), "Total price should be 20");
    }

    @Test
    public void openOrderWithEmptyBooks() {
        Order order = bookStoreService.open(Collections.emptyList(), 0);
        assertNull(order, "Order should be null when no books are provided");
    }

    @Test
    void findBookById() throws BookNotFoundException {
        Book book = bookStoreService.findBookById(1);
        assertNotNull(book);
        assertEquals("crime and punishment", book.getName());
    }

    @Test
    void findOrderById() throws OrderNotFoundException {
        List<Book> books = List.of(new Book(1, "crime and punishment", 20));
        Order order = bookStoreService.open(books, 20);
        Order foundOrder = bookStoreService.findOrderById(order.getId());
        assertNotNull(foundOrder, "Order should not be null");
        assertEquals(order.getId(), foundOrder.getId(), "Order ID should match");
        assertEquals(order.getTotalPrice(), foundOrder.getTotalPrice(), "Total price should match");
    }

    @Test
    public void openOrderCreatesNewOrderId() {
        List<Book> books1 = List.of(new Book(1, "crime and punishment", 20));
        List<Book> books2 = List.of(new Book(2, "the great", 10));
        Order order1 = bookStoreService.open(books1, 20);
        Order order2 = bookStoreService.open(books2, 10);
        assertNotNull(order1);
        assertNotNull(order2);
        assertNotEquals(order1.getId(), order2.getId(), "Each order should have a unique ID");
    }

    @Test
    void completeOrder() {
        List<Book> books = List.of(new Book(1, "crime and punishemnt", 20));
        Order order = bookStoreService.open(books, 20);
        assertNotNull(order);
        Order completedOrder = bookStoreService.complete(order.getId());
        assertEquals(OrderStatus.COMPLETED, completedOrder.getStatus());
    }

    @Test
    void cancelOrder() {
        List<Book> books = List.of(new Book(1, "crime and punishemnt", 20));
        Order order = bookStoreService.open(books, 20);
        assertNotNull(order);
        Order canceledOrder = bookStoreService.cancel(order.getId());
        assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());
    }
}
