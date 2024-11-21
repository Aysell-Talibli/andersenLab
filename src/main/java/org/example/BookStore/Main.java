package org.example.BookStore;

import org.example.BookStore.exceptions.BookNotFoundException;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;
import org.example.BookStore.service.BookStoreService;
import org.example.BookStore.service.OrderFileManager;
import org.example.BookStore.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws BookNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ConfigLoader config = new ConfigLoader("config.properties");
        BookStoreService bookStoreService = new BookStoreService();
        OrderService orderService = new OrderService(bookStoreService, config);
        OrderFileManager orderFileManager=new OrderFileManager(config);
        orderService.displayBooks();
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
                    case "open" -> orderService.openBookOrder(bookStoreService, scanner);
                    case "complete" -> orderService.completeOrder(bookStoreService, scanner);
                    case "cancel" -> orderService.cancelOrder(bookStoreService, scanner);
                    case "list" -> orderService.listOrders(bookStoreService, scanner);
                    case "exit" -> {
                        System.out.println("Exiting program.");
                        orderFileManager.saveState(orderService.getOrders());
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
}
