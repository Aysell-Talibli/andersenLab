package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.BookStore.ConfigLoader;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;
import org.example.BookStore.service.OrderFileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class OrderFileManagerTest {
    private OrderFileManager orderFileManager;
    @Mock
    private ConfigLoader configLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String tempFileLocation = "testOrders.json";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(configLoader.getOrderFileLocation()).thenReturn(tempFileLocation);
        orderFileManager = new OrderFileManager(configLoader);
    }

    @Test
    void saveState() throws IOException {
        String fileLocation = "orders.txt";
        lenient().when(configLoader.getOrderFileLocation()).thenReturn(fileLocation);
        Book book1 = new Book(1, "the great", 20);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        List<Order> orders = new ArrayList<>();
        Order sampleOrder = new Order(1, books, 20);
        orders.add(sampleOrder);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        FileWriter fileWriter = mock(FileWriter.class);
        lenient().doNothing().when(fileWriter).write(anyString());
        orderFileManager.saveState(orders);
        verify(configLoader).getOrderFileLocation();
    }

    @Test
    void loadState() throws IOException {
        String fileLocation = "testOrders.json";
        when(configLoader.getOrderFileLocation()).thenReturn(fileLocation);
        String json = "[{\"id\":1,\"totalPrice\":65,\"status\":\"OPENED\"}]";
        Files.writeString(Paths.get(fileLocation), json);
        orderFileManager.loadState();
        assertEquals(1, orderFileManager.getOrders().size(), "There should be one order loaded");
        assertEquals(1, orderFileManager.getOrders().get(0).getId(), "Order ID should match");

    }
}
