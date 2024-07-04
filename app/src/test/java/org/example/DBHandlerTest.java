package org.example;

import org.example.grpc.TaskProto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DBHandlerTest {
    DBHandler handler;
    BookRepository bookRepository;

    @BeforeEach
    void setup() {
        bookRepository = mock(BookRepository.class);
        handler = new DBHandler(bookRepository);
    }

    @Test
    void addBookHandler() {
        when(bookRepository.addBook(eq("abc"), any(), any(), anyInt())).thenThrow(new Exception());
        assertEquals(3, handler.addBookHandler(TaskProto.Book.newBuilder().setIsbn("abc").setTitle("xyz").addAuthors("auth").setPageCount(3).build()));
        assertEquals(0, handler.addBookHandler(TaskProto.Book.newBuilder().setIsbn("xyz").setTitle("xyz").addAuthors("auth").setPageCount(3).build()));
        verify(bookRepository, times(2)).addBook(eq("abc"), any(), any(), anyInt());
        verify(bookRepository, times(4)).addBook(any(), any(), any(), anyInt());

        /// test scen 2




        //

    }
}