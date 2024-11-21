package org.example.BookStore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.BookStore.ConfigLoader;
import org.example.BookStore.model.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OrderFileManager {
    private final ConfigLoader configLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Order> orders = new ArrayList<>();
    private int lastId=0;

    public OrderFileManager(ConfigLoader configLoader, boolean loadOnInit) {
        this.configLoader = configLoader;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules();
        if (loadOnInit) {
            loadState();
        }
    }

    public void saveState(List<Order> newOrders) {
        String fileLocation = configLoader.getOrderFileLocation();
        try {
            for (Order order : newOrders) {
                    lastId+=1;
                    order.setId(lastId);
            }
            orders.addAll(newOrders);
            String jsonOrder = objectMapper.writeValueAsString(orders);
            Files.writeString(Paths.get(fileLocation), jsonOrder);
        } catch (IOException e) {
            System.out.println("Error happened while saving " + e.getMessage());
        }
    }

    public void loadState() {
        String fileLocation = configLoader.getOrderFileLocation();
        try {
            File file = new File(fileLocation);
            if (file.exists() && file.length() > 0){
                Order[] loadedOrders = objectMapper.readValue(file, Order[].class);
                orders = new ArrayList<>(List.of(loadedOrders));
                lastId = orders.stream()
                        .mapToInt(Order::getId)
                        .max()
                        .orElse(0);
                System.out.println("Orders loaded from" + fileLocation+ lastId);
            }
        } catch (IOException e) {
            System.out.println("Error while loading" + e.getMessage());

        }
    }


}
