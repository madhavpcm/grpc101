package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BookRepository {
    // key-Value store
    private final HashMap<Integer, Book> books = new HashMap<>();
    // isbn <--> key
    private final HashMap<String, Integer> isbnLookup = new HashMap<>();

    private final Random random = new Random();

    /**
     * Checks if the book is already there, and inserts into DB
     * @param isbn Unique book number
     * @param title Title of the book
     * @param authors List of authors of the book
     * @param page_count Number of pages in the book
     * @return returns the generated id of the book on success, throws an error
     */
    public int addBook(String isbn, String title, List<String> authors, int page_count) {
        if (isbnLookup.containsKey(isbn)) {
            throw new IllegalArgumentException("ISBN [" + isbn + "] is already registered.");
        }
        Book newBook = new Book(isbn, title, authors, page_count);
        int id = random.nextInt(Integer.MAX_VALUE);

        books.put(id, newBook);
        isbnLookup.put(isbn, id);

        return id;
    }

    /**
     * TODO implement fields
     * Gets a book from the data store given the randomly generated book id
     * @param id randomly generated book id in the database
     * @param fields list of fields to be filtered
     * @return returns a book with required fields
     */
    public Book getBook(int id, List<Integer> fields) {
        if (!books.containsKey(id))
            throw new IllegalArgumentException("ID [" + id + "] is not valid.");
        return books.get(id);
    }

    /**
     * TODO implement fields
     * Gets a book from the data store given the randomly generated book id
     * @param isbn unique book number
     * @param fields list of fields to be filtered
     * @return returns a book with required fields
     */
    public Book getBook(String isbn, List<Integer> fields) {
        if (!isbnLookup.containsKey(isbn))
            throw new IllegalArgumentException("ISBN [" + isbn + "] is not registered. Please register the book before updating.");
        return books.get(isbnLookup.get(isbn));
    }

    /**
     * Returns all books as a collection
     * @return collection of books
     */
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    /**
     * Updates an existing book with new information, throws error if not
     * @param isbn Unique book number
     * @param title Title of the book
     * @param authors List of authors of the book
     * @param page_count Number of pages in the book
     * @return returns the existing book id in the data store
     */
    public int updateBook(String isbn, String title, List<String> authors, int page_count) {
        if (!isbnLookup.containsKey(isbn)) {
            throw new IllegalArgumentException("ISBN [" + isbn + "] is not registered. Please register the book before updating.");
        }
        Book updatedBook = new Book(isbn, title, authors, page_count);
        int id = isbnLookup.get(isbn);
        books.put(id, updatedBook);
        return id;
    }

    /**
     * Deletes the book from the data store
     * @param bookId randomly generated data store id
     * @return returns the deleted book's id on success
     */
    public int deleteBook(int bookId) {
        if (!books.containsKey(bookId)) {
            throw new IllegalArgumentException("BookID [" + bookId + "] is not valid.");
        }
        isbnLookup.remove(books.get(bookId).getIsbn());
        books.remove(bookId);
        return bookId;
    }

    /**
     * Deletes the book from the data store
     * @param isbn unique book number
     * @return returns the deleted book's id on success
     */
    public int deleteBook(String isbn) {
        if (!isbnLookup.containsKey(isbn)) {
            throw new IllegalArgumentException("ISBN" + isbn + "is not registered.");
        }
        int id = isbnLookup.get(isbn);
        books.remove(isbnLookup.get(isbn));
        isbnLookup.remove(isbn);
        return id;
    }
}