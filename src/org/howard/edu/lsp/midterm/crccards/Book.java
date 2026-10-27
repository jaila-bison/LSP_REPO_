package org.howard.edu.lsp.midterm.crccards;

/**
 * Represents a single book in the Library Lending System.
 * <p>
 * A Book stores its identifying information (ISBN, title, author) and
 * maintains its own availability. It validates borrow and return requests
 * so that a book can never be borrowed twice or returned while already
 * available. Availability can only be changed through {@link #borrowBook()}
 * and {@link #returnBook()}.
 * </p>
 *
 * @author Jaila Walker
 */
public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private boolean available;

    /**
     * Creates a new book. Every new book starts out available.
     *
     * @param isbn   the book's ISBN
     * @param title  the book's title
     * @param author the book's author
     */
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    /**
     * Returns the ISBN of this book.
     *
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Returns the title of this book.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of this book.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Reports whether this book is currently available to borrow.
     *
     * @return {@code true} if the book is available, {@code false} if it is borrowed
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Borrows this book, making it unavailable.
     *
     * @throws IllegalStateException if the book is already borrowed
     */
    public void borrowBook() {
        if (!available) {
            throw new IllegalStateException("Book " + isbn + " is already borrowed.");
        }
        available = false;
    }

    /**
     * Returns this book, making it available again.
     *
     * @throws IllegalStateException if the book is already available
     */
    public void returnBook() {
        if (available) {
            throw new IllegalStateException("Book " + isbn + " is not currently borrowed.");
        }
        available = true;
    }

    /**
     * Returns a description of this book in the form
     * {@code ISBN Title by Author [AVAILABLE]} or {@code ... [BORROWED]}.
     *
     * @return the formatted book details
     */
    @Override
    public String toString() {
        return isbn + " " + title + " by " + author
                + (available ? " [AVAILABLE]" : " [BORROWED]");
    }
}
