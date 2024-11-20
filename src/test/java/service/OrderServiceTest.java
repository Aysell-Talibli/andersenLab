package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.BookStore.ConfigLoader;
import org.example.BookStore.exceptions.BookNotFoundException;
import org.example.BookStore.model.Book;
import org.example.BookStore.model.Order;
import org.example.BookStore.service.BookStoreService;
import org.example.BookStore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private BookStoreService bookStoreService;

    @Mock
    private ConfigLoader configLoader;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(bookStoreService, configLoader);
    }

    @Test
    void openBookOrder() throws BookNotFoundException {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLine())
                .thenReturn("1")
                .thenReturn("done");

        Book book = new Book(1, "crime and punishment", 20);
        when(bookStoreService.findBookById(1)).thenReturn(book);
        when(bookStoreService.open(anyList(), anyDouble())).thenReturn(new Order(1, List.of(book), 20));
        when(bookStoreService.findBookById(1)).thenReturn(book);
        orderService.openBookOrder(bookStoreService, scanner);
        verify(bookStoreService, times(1)).findBookById(1);
        verify(bookStoreService, times(1)).open(anyList(), anyDouble());
    }

    @Test
    void completeOrder() {
        Scanner scanner = new Scanner("1\ndone\n");
        orderService.completeOrder(bookStoreService, scanner);
        verify(bookStoreService, times(1)).complete(1);
    }

    @Test
    void cancelOrder() {
        Scanner scanner = new Scanner("1\ndone\n");
        orderService.cancelOrder(bookStoreService, scanner);
        verify(bookStoreService, times(1)).cancel(1);
    }

    @Test
    void listOrders() {
        List<Order> mockOrders = Arrays.asList(
                new Order(1, List.of(new Book(1, "crime and punishment", 20)), 20),
                new Order(2, List.of(new Book(2, "The Great", 10)), 30)
        );
        when(bookStoreService.listOrders(1, 5, "id")).thenReturn(mockOrders);
        Scanner scanner = new Scanner("1\n5\nid\n");
        orderService.listOrders(bookStoreService, scanner);
        verify(bookStoreService, times(1)).listOrders(1, 5, "id");
    }

    @Test
    void saveState() throws IOException {
        String fileLocation = "orders.txt";
        lenient().when(configLoader.getOrderFileLocation()).thenReturn(fileLocation);
        Book book1=new Book(1,"the great",20);
        List<Book> books= new ArrayList<>();
        books.add(book1);
        List<Order> orders = new ArrayList<>();
        Order sampleOrder = new Order(1,books, 20);
        orders.add(sampleOrder);
        orderService.getOrders().addAll(orders);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        FileWriter fileWriter = mock(FileWriter.class);
        lenient().doNothing().when(fileWriter).write(anyString());
        orderService.saveState();
        verify(configLoader).getOrderFileLocation();
    }

    @Test
    void loadState() throws IOException {
        String fileLocation = "testOrders.json";
        when(configLoader.getOrderFileLocation()).thenReturn(fileLocation);
        String json = "[{\"id\":1,\"totalPrice\":65,\"status\":\"OPENED\"}]";
        Files.writeString(Paths.get(fileLocation), json);
        orderService.loadState();
        assertEquals(1, orderService.getOrders().size());
        assertEquals(1, orderService.getOrders().get(0).getId());
    }

    @Test
    void changeBookAvailabilityEnabled() throws BookNotFoundException {
        int bookId = 1;
        boolean isAvailable = true;
        when(configLoader.isBookAvailabilityChangeEnabled()).thenReturn(true);
        orderService.changeBookAvailability(bookId, isAvailable);
        verify(bookStoreService).changeAvailability(bookId, isAvailable);
    }

    @Test
    void changeBookAvailabilityDisabled() throws BookNotFoundException {
        int bookId = 1;
        boolean isAvailable = true;
        when(configLoader.isBookAvailabilityChangeEnabled()).thenReturn(false);
        orderService.changeBookAvailability(bookId, isAvailable);
        verify(bookStoreService, never()).changeAvailability(anyInt(), anyBoolean());
    }

}
