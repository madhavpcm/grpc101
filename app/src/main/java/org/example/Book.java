package org.example;

import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private List<String> authors;
    private int pageCount;

    public Book(String isbn, String title, List<String> authors, int pageCount) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", pageCount=" + pageCount +
                '}';
    }
}
