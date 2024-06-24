package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.BookServiceGrpc;
import org.example.grpc.TaskProto;


import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.StatusRuntimeException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link GRPCServer}.
 */
public class GRPCClient {
    private static final Logger logger = Logger.getLogger(GRPCClient.class.getName());

    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    /** Construct client for accessing HelloWorld server using the existing channel. */
    public GRPCClient(Channel channel) {
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    /** Say hello to server. */
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
            logger.log(Level.WARNING,  response.getResponse());
        }

    }
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
            logger.log(Level.WARNING,  response.getResponse());
        }

    }
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
            logger.log(Level.WARNING,  response.getResponse());
        }
    }
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
            logger.log(Level.WARNING,  response.getResponse());
        }
    }
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
            logger.info("Delete Transaction successful for Book ID: " + response.getData(0));
        } else {
            logger.log(Level.WARNING,  response.getResponse());
        }

    }
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
            logger.info("Delete Transaction successful for Book ID: " + response.getData(0));
        } else {
            logger.log(Level.WARNING,  response.getResponse());
        }

    }

    public static void main(String[] args) throws Exception {
        String host= "localhost:9090";

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

