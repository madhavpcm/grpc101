package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import org.example.grpc.BookServiceGrpc;

import java.io.IOException;
import java.util.stream.Stream;

import org.example.grpc.TaskProto;


public class GRPCServer {
    private static final DBHandler handler = new DBHandler();

    public static void main(String[] args) throws IOException, InterruptedException {
        //start server on port 9090
        Server server = ServerBuilder.forPort(9090)
                .addService(new ServerHandler())
                .build()
                .start();

        System.out.println("Server started, listening on " + server.getPort());

        //server loop
        server.awaitTermination();
    }

    // Implementation of the Greeter service
    static class ServerHandler extends BookServiceGrpc.BookServiceImplBase {

        @Override
        public void addBook(TaskProto.BookPutRequest request, StreamObserver<TaskProto.BookResponseGeneric> responseObserver) {
            // Create a response
            TaskProto.BookResponseGeneric response = null;
            try {
                int status = handler.addBookHandler(request.getBook());
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(status)
                        .setResponse("OK")
                        .build();

            } catch (Exception e){
                // internal server error
                response = TaskProto.BookResponseGeneric.newBuilder()
                        .setBookId(-1)
                        .setResponse(e.getMessage())
                        .build();
            }
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void updateBook(TaskProto.BookPutRequest request, StreamObserver<TaskProto.BookResponseGeneric> responseObserver ) {
            TaskProto.BookResponseGeneric response = null;
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

        @Override
        public void deleteBook(TaskProto.BookIDRequest request, StreamObserver<TaskProto.BookResponseGeneric> responseObserver) {
            TaskProto.BookResponseGeneric response = null;
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

        @Override
        public void getBooks(TaskProto.BookQueryRequest request, StreamObserver<TaskProto.BookQueryResponse> responseObserver) {
            TaskProto.BookQueryResponse response = null;
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


