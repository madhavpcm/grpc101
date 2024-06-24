package org.example;

import io.grpc.ManagedChannel;
import org.example.grpc.BookServiceGrpc;
import org.example.grpc.TaskProto;


import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.StatusRuntimeException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * A simple client that does CRUD ops from {@link GRPCServer}.
 */
public class GRPCClient {
    private static final Logger logger = Logger.getLogger(GRPCClient.class.getName());

    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    /** Constructor, boilerplate GRPC */
    public GRPCClient(Channel channel) {
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    /**
     *  {@summary This function is the client side interface for the RPC service AddBook}
     * */
    public void addBook(String isbn, String title, List<String> authors, int page_count) {
        logger.info("Adding book with ISBN" + isbn);
        TaskProto.Book payload = TaskProto.Book.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .addAllAuthors(authors)
                .setPageCount(page_count)
                .build();

        TaskProto.BookPutRequest request = TaskProto.BookPutRequest.newBuilder()
                .setBook(payload)
                .build();

        TaskProto.BookResponseGeneric response;
        try {
            response = blockingStub.addBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if(response.getBookId() != -1) {
            logger.info("Add Transaction successful, Received Book ID: " + response.getBookId());
        } else {
            logger.log(Level.WARNING, "Add Transaction failed, Reason: {0}",  response.getResponse());
        }

    }
    /**
     * {@summary This function is the client side interface for the RPC service AddBook}
     * @param isbn Unique ISBN of the book
     * */
    public void updateBook(String isbn, String title, List<String> authors, int page_count) {
        logger.info("Adding book " + isbn+ " ... ");
        TaskProto.Book payload = TaskProto.Book.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .addAllAuthors(authors)
                .setPageCount(page_count)
                .build();

        TaskProto.BookPutRequest request = TaskProto.BookPutRequest.newBuilder()
                .setBook(payload)
                .build();

        TaskProto.BookResponseGeneric response;
        try {
            response = blockingStub.updateBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if(response.getBookId() != -1) {
            logger.info("Update Transaction successful, Received Book ID: " + response.getBookId());
        } else {
            logger.log(Level.WARNING, "Update Transaction failed, Reason: {0}",  response.getResponse());
        }

    }

    /**
     *
     * @param book_id
     */
    public void deleteBook(int book_id) {
        logger.info("Deleting book with bookID : " + book_id+ " ... ");
        TaskProto.BookIdentifier payload = TaskProto.BookIdentifier.newBuilder()
                .setBookId(book_id)
                .build();

        TaskProto.BookIDRequest request = TaskProto.BookIDRequest.newBuilder()
                .setId(payload)
                .build();

        TaskProto.BookResponseGeneric response;
        try {
            response = blockingStub.deleteBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if(response.getBookId() != -1) {
            logger.info("Delete Transaction successful for Book ID: " + response.getBookId());
        } else {
            logger.log(Level.WARNING, "Delete Transaction failed, Reason: {0}",  response.getResponse());
        }
    }

    /**
     *
     * @param isbn
     */
    public void deleteBook(String isbn) {
        logger.info("Deleting book with ISBN : " + isbn+ " ... ");
        TaskProto.BookIdentifier payload = TaskProto.BookIdentifier.newBuilder()
                .setBookIsbn(isbn)
                .build();

        TaskProto.BookIDRequest request = TaskProto.BookIDRequest.newBuilder()
                .setId(payload)
                .build();

        TaskProto.BookResponseGeneric response;
        try {
            response = blockingStub.deleteBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if(response.getBookId() != -1) {
            logger.info("Delete Transaction successful for Book ID: " + response.getBookId());
        } else {
            logger.log(Level.WARNING, "Delete Transaction failed, Reason: {0}",  response.getResponse());
        }
    }

    /**
     *
     * @param book_id
     * @param fields
     */
    public void getBooks(int book_id, List<Integer> fields) {
        logger.info("Getting book with bookId : " + book_id+ " ... ");
        TaskProto.BookIdentifier payload = TaskProto.BookIdentifier.newBuilder()
                .setBookId(book_id)
                .build();

        TaskProto.BookQueryRequest request = TaskProto.BookQueryRequest.newBuilder()
                .setId(payload)
                .addAllFields(fields)
                .build();

        TaskProto.BookQueryResponse response;
        try {
            response = blockingStub.getBooks(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if(response.getDataCount() != 0) {
            logger.info("Got Book:\n" + response.getData(0));
        } else {
            logger.log(Level.WARNING,  response.getResponse());
        }

    }

    /**
     *
     * @param isbn
     * @param fields
     */
    public void getBooks(String isbn, List<Integer> fields) {
        logger.info("Getting book with ISBN : " + isbn+ " ... ");
        TaskProto.BookIdentifier payload = TaskProto.BookIdentifier.newBuilder()
                .setBookIsbn(isbn)
                .build();

        TaskProto.BookQueryRequest request = TaskProto.BookQueryRequest.newBuilder()
                .setId(payload)
                .addAllFields(fields)
                .build();

        TaskProto.BookQueryResponse response;
        try {
            response = blockingStub.getBooks(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if(response.getDataCount() != 0) {
            logger.info("Got Book: %n" + response.getData(0));
        } else {
            logger.log(Level.WARNING,  response.getResponse());
        }

    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String host= "localhost:9090";

        // Logging setup, dont want dates and stuff
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);
        for (var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);

        ManagedChannel channel = Grpc.newChannelBuilder(host, InsecureChannelCredentials.create())
                .build();
        try {
            GRPCClient client = new GRPCClient(channel);
            // success
            client.addBook("123", "mocking bird", Arrays.asList(), 4 );
            // success
            client.addBook("125", "mocking bird 2", Arrays.asList("Dave Lee", "John Doe"), 4 );
            // success
            client.updateBook("125", "mocking bird 3", Arrays.asList("Dave Lee", "John Doe"), 4 );
            // success
            client.getBooks("125", Arrays.asList());
            // fail
            client.getBooks("12", Arrays.asList());
            // fail
            client.deleteBook("1");
            // success
            client.deleteBook("125");
            // fail
            client.addBook("123", "MocKingBird", Arrays.asList(), 2);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}

