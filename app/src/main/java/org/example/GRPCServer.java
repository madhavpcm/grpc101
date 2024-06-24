package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import org.example.grpc.BookServiceGrpc;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.grpc.TaskProto;


public class GRPCServer {
    private static final DBHandler handler = new DBHandler();
    private static final Logger logger = Logger.getLogger(GRPCClient.class.getName());

    /**
     * {@summary Server main function}
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        //start server on port 9090
        Server server = ServerBuilder.forPort(9090)
                .addService(new ServerHandler())
                .build()
                .start();

        //Logger setup
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);
        for (var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);

        logger.info("Server started listening on port 9090");

        //server loop
        server.awaitTermination();
    }

    // Implementation of the Greeter service
    /**
     * {@summary Server handler interface, which overrides the default gRPC server-side endpoints}
     * This class handles all exceptions and returns appropriate response to the client
     */
    static class ServerHandler extends BookServiceGrpc.BookServiceImplBase {

        /**
         * {@summary override for .proto defined AddBook's endpoint}
         * @param request .proto defined request
         * @param responseObserver .proto defined response
         */
        @Override
        public void addBook(TaskProto.BookPutRequest request, StreamObserver<TaskProto.BookResponseGeneric> responseObserver) {
            // Create a response
            TaskProto.BookResponseGeneric response;
            try {
                int status = handler.addBookHandler(request.getBook());
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(status)
                        .setResponse("OK")
                        .build();

            } catch (Exception e){
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(-1)
                        .setResponse(e.getMessage())
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        /**
         * {@summary override for .proto defined UpdateBook's endpoint}
         * @param request .proto defined request
         * @param responseObserver .proto defined response
         */
        @Override
        public void updateBook(TaskProto.BookPutRequest request, StreamObserver<TaskProto.BookResponseGeneric> responseObserver ) {
            TaskProto.BookResponseGeneric response;
            try {
                int status = handler.updateBookHandler(request.getBook());
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(status)
                        .setResponse("OK")
                        .build();
            } catch (Exception e) {
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(-1)
                        .setResponse(e.getMessage())
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        /**
         * {@summary override for .proto defined DeleteBook's endpoint}
         * @param request .proto defined request
         * @param responseObserver .proto defined response
         */
        @Override
        public void deleteBook(TaskProto.BookIDRequest request, StreamObserver<TaskProto.BookResponseGeneric> responseObserver) {
            TaskProto.BookResponseGeneric response;
            try {
                int status = handler.deleteBookHandler(request.getId());
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(status)
                        .setResponse("OK")
                        .build();
            } catch (Exception e) {
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(-1)
                        .setResponse(e.getMessage())
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        /**
         * {@summary override for .proto defined GetBooks' endpoint}
         * @param request .proto defined request
         * @param responseObserver .proto defined response
         */
        @Override
        public void getBooks(TaskProto.BookQueryRequest request, StreamObserver<TaskProto.BookQueryResponse> responseObserver) {
            TaskProto.BookQueryResponse response;
            try {
                Book data = handler.getBooksHandler(request.getId(), request.getFieldsList());
                TaskProto.Book payload = TaskProto.Book.newBuilder()
                        .setIsbn(data.getIsbn())
                        .setTitle(data.getTitle())
                        .addAllAuthors(data.getAuthors())
                        .setPageCount(data.getPageCount())
                        .build();

                response = TaskProto.BookQueryResponse.newBuilder()
                        .addData(payload)
                        .setResponse("OK")
                        .build();

            } catch (Exception e) {
                response = TaskProto.BookQueryResponse.newBuilder()
                        .setResponse(e.getMessage())
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }
}


