package org.example.BookStore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.BookStore.ConfigLoader;
import org.example.BookStore.exceptions.BookNotFoundException;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderService {
    private final BookStoreService bookStoreService;
    private final ConfigLoader configLoader;
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public OrderService(BookStoreService bookStoreService, ConfigLoader configLoader) {
        this.bookStoreService = bookStoreService;
        this.configLoader=configLoader;
    }

    public void displayBooks() throws BookNotFoundException {
        System.out.println("Available books: ");
        changeBookAvailability(1, true);
        bookStoreService.getBooks().forEach(System.out::println);
    }

    public void openBookOrder(BookStoreService bookStoreService,
                              Scanner scanner) {
        List<Book> chosenBooks = new ArrayList<>();
        double totalPrice = 0;
        System.out.println("Enter the book Id you wanna order" +
                " one by one, or type 'done' when finished:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done") ||
                    input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int id = Integer.parseInt(input);
                Book book = bookStoreService.findBookById(id);
                chosenBooks.add(book);
                totalPrice += book.getPrice();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid book ID.");
            } catch (BookNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        Order orderedBook = bookStoreService.open(chosenBooks, totalPrice);
        if (orderedBook == null) {
            System.out.println("orderedBook is null. Cannot display details.");
            return;
        } else {
            displayOrderDetails(orderedBook, chosenBooks, totalPrice);
        }
        orders.add(orderedBook);
        chosenBooks.clear();
        totalPrice = 0;
    }

    public void displayOrderDetails(Order orderedBook, List<Book> chosenBooks, double totalPrice) {
        System.out.println("Order Details: Order " + orderedBook.getId());
        System.out.printf("%-5s %-30s %-10s%n", "ID", "Title", "Price");
        System.out.println("--------------------------------------------");
        for (Book book : chosenBooks) {
            System.out.printf("%-5d %-30s $%-9.2f%n", book.getId(), book.getName(), book.getPrice());
        }
        System.out.printf("Total Price: $%.2f%n", totalPrice);
    }

    public void completeOrder(BookStoreService bookStoreService,
                              Scanner scanner) {
        System.out.println("Enter the order IDs you want" +
                " to complete, one by one, or type 'done' when finished:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done") ||
                    input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int orderId = Integer.parseInt(input);
                bookStoreService.complete(orderId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid order ID or 'done' .");
            }

        }
    }

    public void cancelOrder(BookStoreService bookStoreService, Scanner scanner) {
        System.out.println("Enter the order ID you want to cancel:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done") ||
                    input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int orderId = Integer.parseInt(input);
                bookStoreService.cancel(orderId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid order ID or 'done'");
            }
        }
    }

    public void listOrders(BookStoreService bookStoreService, Scanner scanner) {
        System.out.println("Enter page number:");
        int page = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter page size:");
        int size = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter field to sort by (id, totalPrice, opening, closing, status):");
        String sortBy = scanner.nextLine();
        List<Order> paginatedOrders = bookStoreService.listOrders(page, size, sortBy);
        paginatedOrders.forEach(order -> {
            System.out.println("Order ID: " + order.getId() +
                    ", Total Price: " + order.getTotalPrice() +
                    ", Status: " + order.getStatus() +
                    ", Opening Time: " + order.getOpeningTime() +
                    ", Closing Time: " + (order.getClosingTime() != null ? order.getClosingTime() : "Not closed yet"));
        });
    }

    public void changeBookAvailability(int bookId, boolean isAvailable) throws BookNotFoundException {
        if (configLoader.isBookAvailabilityChangeEnabled()) {
            bookStoreService.changeAvailability(bookId, isAvailable);
            System.out.println("Book availability changed ");
        } else {
            System.out.println("Changing book availability is disabled in the configuration.");
        }
    }
}

