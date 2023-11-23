package com.sheffield;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    // Insert a new book into the database
    public void insertBook(Book newBook, Connection connection) throws SQLException {
        // TODO : Insert Book operation.
    }

    // Get all books from the database
    public void getAllBooks(Connection connection) throws SQLException {
        // TODO : Get All Book operation.
    }

    // Get a book by ISBN
    public void getBookByISBN(String isbn, Connection connection) throws SQLException {
        // TODO : Get Book by ISBN operation.
    }

    // Update an existing book in the database
    public void updateBook(Book newBook, Connection connection) throws SQLException {
        // TODO : Update Book operation.
    }

    // Delete a book from the database by ISBN
    public void deleteBook(String isbn, Connection connection) throws SQLException {
        // TODO : Delete Book operation.
    }
}
