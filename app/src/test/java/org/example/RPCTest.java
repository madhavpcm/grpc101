package org.example;

import io.grpc.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RPCTest {
    private static final Logger logger = Logger.getLogger(GRPCClient.class.getName());

    private static GRPCServer grpcServer;
    private static Server server;
    private static ManagedChannel channel;
    private static String host = "localhost:9090";

    @BeforeEach
    public void setUp() throws IOException {
        // Start the gRPC server
        server = ServerBuilder.forPort(9090)
                .addService(new GRPCServer.ServerHandler())
                .build()
                .start();

        // Create a channel to communicate with the server
        channel = Grpc.newChannelBuilder(host, InsecureChannelCredentials.create())
                .build();
    }

    @AfterEach
    public void tearDown() {
        // Shut down the channel
        if (channel != null) {
            channel.shutdown();
        }

        // Stop the gRPC server
        if (grpcServer != null) {
            if(server != null) {
                server.shutdown();
            }
        }

        // Force clean the server and client instances
        grpcServer = null;
        channel = null;
        System.gc();
    }

    @Test
    public void Test1() {
        GRPCClient client = new GRPCClient(channel);
        Book  B1 = new Book("1", "Mocking Bird", Arrays.asList("Dave", "Lee"), 400);
        Book  B2 = new Book("2", "Man killer", Arrays.asList("Rock", "Lee"), 450);
        Book  B3 = new Book("3", "Man Dinner", Arrays.asList("Gray","Rock", "Lee"), 250);
        Book  B4 = new Book("1", "Grid 3", Arrays.asList("Ron", "Wake"), 403);

        //test add
        int  b1 = client.addBook("1", "Mocking Bird", Arrays.asList("Dave", "Lee"), 400);
        int  b2 = client.addBook("2", "Man killer", Arrays.asList("Rock", "Lee"), 450);
        int  b3 = client.addBook("3", "Man Dinner", Arrays.asList("Gray","Rock", "Lee"), 250);

        assertEquals(client.getBooks(b1, Arrays.asList()), B1);
        assertEquals(client.getBooks(b2, Arrays.asList()), B2);
        assertEquals(client.getBooks(b3, Arrays.asList()), B3);

        // test update
        int u1 = client.updateBook("1", "Grid 3", Arrays.asList("Ron", "Wake"), 403);
        assertEquals(client.getBooks(u1, Arrays.asList()), B4);

        // test get
        assertEquals(client.getBooks(-1, Arrays.asList()), null);

        // test delete
        int u2 = client.deleteBook(u1);
        assertEquals(u1, u2);
        logger.info("Test passed");
    }

    public static void main(String args[]) {
        //Logger setup
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);
        for (var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);

        grpcServer = new GRPCServer();
        logger.info("Beginning tests...");
    }
}