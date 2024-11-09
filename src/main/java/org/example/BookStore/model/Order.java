package org.example.BookStore.model;

import org.example.BookStore.model.Book;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private List<Book> books;
    private double totalPrice;
    private OrderStatus status;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;


    public Order(int id, List<Book> books, double totalPrice) {
        this.id = id;
        this.books = books;
        this.totalPrice=totalPrice;
        this.status=OrderStatus.OPENED;
        this.openingTime=LocalDateTime.now();
    }


    public int getId() {
        return id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public LocalDateTime getClosingTime() {
        return closingTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        if(status==OrderStatus.COMPLETED){
            this.closingTime=LocalDateTime.now();
        }
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + id +
                ", books=" + books +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }
}