package org.example.BookStore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
    private int id;
    private String name;
    private double price;
    private boolean available;


    @JsonCreator
    public Book(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("price") double price) {
        this.id = id;
        this.name = name;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}
