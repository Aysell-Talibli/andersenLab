package org.example.BookStore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.BookStore.ConfigLoader;
import org.example.BookStore.model.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderFileManager {
    private final ConfigLoader configLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Order> orders = new ArrayList<>();

    public OrderFileManager(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules();
    }

    public void saveState(List<Order> orders) {
        String fileLocation = configLoader.getOrderFileLocation();
        try (FileWriter fileWriter = new FileWriter(fileLocation, true)) {
            for (Order order : orders) {
                String jsonOrder = objectMapper.writeValueAsString(order);
                fileWriter.write(jsonOrder + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error happened while saving " + e.getMessage());
        }
    }

    public void loadState() {
        objectMapper.findAndRegisterModules();
        String fileLocation = configLoader.getOrderFileLocation();
        try {
            File file = new File(fileLocation);
            if (file.exists()) {
                Order[] loadedOrders = objectMapper.readValue(file, Order[].class);
                orders = new ArrayList<>(List.of(loadedOrders));
                System.out.println("Orders loaded from " + fileLocation);
            }
        } catch (IOException e) {
            System.out.println("Error while loading" + e.getMessage());

        }
    }

    public List<Order> getOrders() {
        return orders;
    }
}
