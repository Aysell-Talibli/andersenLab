package org.example.BookStore.service;

import org.example.BookStore.exceptions.BookNotFoundException;
import org.example.BookStore.exceptions.OrderNotFoundException;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;
import org.example.BookStore.model.OrderStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookStoreService {
    private List<Book> books = new ArrayList<Book>();
    private List<Order> orders = new ArrayList<Order>();
    int orderId = 1;

    public BookStoreService() {

        books.add(new Book(1, "crime and punishemnt", 20));
        books.add(new Book(2, "The Great", 15));
        books.add(new Book(3, "Anna Karenina", 25));
        books.add(new Book(4, "Time Machine", 10));
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Order> getOrders() {

        return orders;
    }

    public List<Order> listOrders(int page, int size, String sortBy) {
        Comparator<Order> comparator;
        switch (sortBy.toLowerCase()) {
            case "id":
                comparator = Comparator.comparing(Order::getId);
                break;
            case "totalprice":
                comparator = Comparator.comparing(Order::getTotalPrice);
                break;
            case "opening":
                comparator = Comparator.comparing(Order::getOpeningTime);
                break;
            case "status":
                comparator = Comparator.comparing(Order::getStatus);
                break;
            default:
                throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        return orders.stream()
                .sorted(comparator)
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Order open(List<Book> books, double totalPrice) {
        if (books.isEmpty()) {
            System.out.println("No books ordered");
            return null;
        }
        List<Book> booksCopy = new ArrayList<>(books);
        Order order = new Order(orderId++, booksCopy, totalPrice);
        orders.add(order);
        return order;
    }

    public Book findBookById(int bookId) throws BookNotFoundException {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        throw new BookNotFoundException("Book with id " + bookId + " not found.");
    }

    public Order findOrderById(int orderid) throws OrderNotFoundException {
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new OrderNotFoundException("Order with id " + orderId + " not found");
    }

    public Order complete(int orderId) {
        try {
            Order order = findOrderById(orderId);
            if (!order.getStatus().equals(OrderStatus.OPENED)) {
                System.out.println("Order is already completed or in another state.");
                return order;
            }
            order.setStatus(OrderStatus.COMPLETED);
            System.out.println("Order " + orderId + " completed");
            return order;
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Order cancel(int orderId) {
        try {
            Order order = findOrderById(orderId);
            if (!order.getStatus().equals(OrderStatus.OPENED)) {
                System.out.println("Order is already canceled or in another state.");
                return order;
            }
            order.setStatus(OrderStatus.CANCELED);
            System.out.println("Order " + orderId + " canceled");
            return order;
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


}