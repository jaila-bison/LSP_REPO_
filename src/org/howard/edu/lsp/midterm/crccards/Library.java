package org.howard.edu.lsp.midterm.crccards;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a collection of {@link Book} objects in the Library Lending System.
 * <p>
 * The Library adds and finds books, coordinates borrowing and returning by
 * locating the book and delegating the state change to it, enforces a limit
 * of three books borrowed at one time, and retrieves books by criteria.
 * Books are stored in a {@link HashMap} keyed by ISBN, which gives fast
 * lookup and makes duplicate-ISBN detection straightforward.
 * </p>
 *
 * @author Jaila Walker
 */
public class Library {

    /** Maximum number of books that may be borrowed from the library at one time. */
    private static final int MAX_BORROWED = 3;

    private final Map<String, Book> books;
    private int borrowedCount;

    /**
     * Creates an empty library.
     */
    public Library() {
        this.books = new HashMap<>();
        this.borrowedCount = 0;
    }

    /**
     * Adds a book to the library.
     *
     * @param book the book to add
     * @throws IllegalArgumentException if the book is null or a book with the
     *                                  same ISBN is already in the library
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Duplicate ISBN: " + book.getIsbn());
        }
        books.put(book.getIsbn(), book);
    }

    /**
     * Finds the book with the given ISBN.
     *
     * @param isbn the ISBN to look up
     * @return the matching Book instance, or {@code null} if none exists
     */
    public Book findBook(String isbn) {
        return books.get(isbn);
    }

    /**
     * Borrows the book with the given ISBN. The library locates the book,
     * checks the three-book limit, and delegates the state change to the book.
     * A failed request changes nothing.
     *
     * @param isbn the ISBN of the book to borrow
     * @throws IllegalArgumentException if no book with that ISBN is in the library
     * @throws IllegalStateException    if the book is already borrowed or the
     *                                  three-book limit has been reached
     */
    public void borrowBook(String isbn) {
        Book book = requireBook(isbn);
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book " + isbn + " is already borrowed.");
        }
        if (borrowedCount >= MAX_BORROWED) {
            throw new IllegalStateException("Borrowing limit of " + MAX_BORROWED + " reached.");
        }
        book.borrowBook();
        borrowedCount++;
    }

    /**
     * Returns the book with the given ISBN. The library locates the book and
     * delegates the state change to it. A failed request changes nothing.
     *
     * @param isbn the ISBN of the book to return
     * @throws IllegalArgumentException if no book with that ISBN is in the library
     * @throws IllegalStateException    if the book is not currently borrowed
     */
    public void returnBook(String isbn) {
        Book book = requireBook(isbn);
        book.returnBook();
        borrowedCount--;
    }

    /**
     * Returns all books that are currently available.
     *
     * @return a list of available books; empty if there are none
     */
    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                result.add(book);
            }
        }
        return result;
    }

    /**
     * Returns all books whose author exactly matches the given name
     * (case-sensitive), including borrowed books.
     *
     * @param author the author name to match
     * @return a list of matching books; empty if there are none
     */
    public List<Book> getBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().equals(author)) {
                result.add(book);
            }
        }
        return result;
    }

    /**
     * Returns all books sorted by ISBN in ascending lexicographical order,
     * regardless of insertion order or borrowing status. The library's
     * contents and the books' states are not changed.
     *
     * @return a new sorted list of all books; empty if the library is empty
     */
    public List<Book> getBooksSortedByIsbn() {
        List<Book> result = new ArrayList<>(books.values());
        result.sort(Comparator.comparing(Book::getIsbn));
        return result;
    }

    /**
     * Returns the number of books currently borrowed from the library.
     *
     * @return the current borrowed-book count
     */
    public int getBorrowedBookCount() {
        return borrowedCount;
    }

    /**
     * Looks up a book by ISBN, failing if it is not in the library.
     *
     * @param isbn the ISBN to look up
     * @return the matching book
     * @throws IllegalArgumentException if no book with that ISBN exists
     */
    private Book requireBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new IllegalArgumentException("No book with ISBN " + isbn + " in the library.");
        }
        return book;
    }
}
