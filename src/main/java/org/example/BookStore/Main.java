package org.example.BookStore;

import org.example.BookStore.exceptions.BookNotFoundException;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;
import org.example.BookStore.service.BookStoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookStoreService bookStoreService = new BookStoreService();
        System.out.println("Available books: ");
        List<Book> allBooks = bookStoreService.getBooks();
        List<Book> chosenBooks = new ArrayList<Book>();
        double totalPrice = 0;
        for (Book book : allBooks) {
            System.out.println(book);
        }

        while (true) {
            System.out.println(""" 
                    Options:
                    open - Open book by ID to add to your order 
                    complete - Complete an order by ID 
                    cancel - Cancel an order by ID 
                    list - List all orders 
                    exit - Exit the program
                    """);
            String option = scanner.nextLine();
            try {
                switch (option.toLowerCase()) {
                    case "open" -> openBookOrder(bookStoreService, scanner, chosenBooks, totalPrice);
                    case "complete" -> completeOrder(bookStoreService, scanner);
                    case "cancel" -> cancelOrder(bookStoreService, scanner);
                    case "list" -> listOrders(bookStoreService, scanner);
                    case "exit" -> {
                        System.out.println("Exiting program.");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try the options.");

                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    public static void openBookOrder(BookStoreService bookStoreService,
                                     Scanner scanner, List<Book> chosenBooks,
                                     double totalPrice) {
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
        displayOrderDetails(orderedBook, chosenBooks, totalPrice);
        chosenBooks.clear();
        totalPrice = 0;
    }

    private static void displayOrderDetails(Order orderedBook, List<Book> chosenBooks, double totalPrice) {
        System.out.println("Order Details: Order " + orderedBook.getId());
        System.out.printf("%-5s %-30s %-10s%n", "ID", "Title", "Price");
        System.out.println("--------------------------------------------");
        for (Book book : chosenBooks) {
            System.out.printf("%-5d %-30s $%-9.2f%n", book.getId(), book.getName(), book.getPrice());
        }
        System.out.printf("Total Price: $%.2f%n", totalPrice);
    }

    public static void completeOrder(BookStoreService bookStoreService,
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

    private static void cancelOrder(BookStoreService bookStoreService, Scanner scanner) {
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

    private static void listOrders(BookStoreService bookStoreService, Scanner scanner) {
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


}
