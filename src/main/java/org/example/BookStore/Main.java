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
        Scanner scanner=new Scanner(System.in);
        BookStoreService bookStoreService=new BookStoreService();
        System.out.println("Available books: ");
        List<Book> allBooks=bookStoreService.getBooks();
        List<Book>chosenBooks=new ArrayList<Book>();
        double totalPrice=0;
        for(Book book:allBooks){
            System.out.println(book);
        }

        while(true){
            System.out.println("\nOptions:" +
                    "\n open - Open book by ID to add to your order" +
                    "\n complete - Complete an order by ID" +
                    "\n cancel - Cancel an order by ID" +
                    "\n list - List all orders" +
                    "\n exit - Exit the program");
            String option=scanner.nextLine();
            try{
                switch (option.toLowerCase()){
                    case "open":
                        System.out.println("Enter the book Id you wanna order one by one, or type 'done' when finished:");
                        while(true){
                            String input=scanner.nextLine();
                            if(input.equalsIgnoreCase("done")){
                                break;
                            }
                            try{
                                int id=Integer.parseInt(input);
                                Book book = bookStoreService.findBookById(id);
                                chosenBooks.add(book);
                                totalPrice += book.getPrice();
                            }
                            catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid book ID.");
                            } catch (BookNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        Order orderedBook=bookStoreService.open(chosenBooks,totalPrice);
                        System.out.println("Order Details:Order "+orderedBook.getID());
                        System.out.printf("%-5s %-30s %-10s%n", "ID", "Title", "Price");
                        System.out.println("--------------------------------------------");
                        for (Book book : chosenBooks) {
                            System.out.printf("%-5d %-30s $%-9.2f%n", book.getID(), book.getName(), book.getPrice());
                        }
                        System.out.printf("Total Price: $%.2f%n", totalPrice);
                        chosenBooks.clear();
                        totalPrice=0;
                        break;
                    case "complete":
                        System.out.println("Enter the order IDs you want to complete, one by one, or type 'done' when finished:");
                        while (true) {
                            String input = scanner.nextLine();
                            if (input.equalsIgnoreCase("done")) {
                                break;
                            }
                            try{
                                int orderId = Integer.parseInt(input);
                                bookStoreService.complete(orderId);
                            }
                            catch(NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid order ID or 'done' .");
                            }
                        }

                        break;

                    case "cancel" :
                        System.out.println("Enter the order id you wanna cancel ");
                        while (true) {
                            String input = scanner.nextLine();
                            if (input.equalsIgnoreCase("done")) {
                                break;
                            }
                            try{
                                int orderId = Integer.parseInt(input);
                                bookStoreService.cancel(orderId);}
                            catch(NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid order ID or 'done'");
                            }
                        }

                        break;
                    case "list" :
                        System.out.println("Enter page number:");
                        int page = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter page size:");
                        int size = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter field to sort by (id, totalPrice, opening, closing, status):");
                        String sortBy = scanner.nextLine();
                        List<Order> paginatedOrders = bookStoreService.listOrders(page, size, sortBy);
                        paginatedOrders.forEach(order -> {
                            System.out.println("Order ID: " + order.getID() +
                                    ", Total Price: " + order.getTotalPrice() +
                                    ", Status: " + order.getStatus() +
                                    ", Opening Time: " + order.getOpeningTime() +
                                    ", Closing Time: " + (order.getClosingTime() != null ? order.getClosingTime() : "Not closed yet"));
                        });
                        break;
                    case "exit":
                        System.out.println("Exiting program.");
                        scanner.close();
                        return;

                }}
            catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }



        }


    }
}
