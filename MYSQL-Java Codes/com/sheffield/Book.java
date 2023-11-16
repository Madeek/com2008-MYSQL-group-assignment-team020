package com.sheffield;
import java.math.BigDecimal;

public class Book {
    private String isbn;
    private String title;
    private String authorName;
    private int publicationYear;
    private String genre;
    private BigDecimal price;

    // Constructor to initialize a Book object with its attributes
    public Book(String isbn, String title, String authorName, int publicationYear, String genre, BigDecimal price) {
        this.setIsbn(isbn);
        this.setTitle(title);
        this.setAuthorName(authorName);
        this.setPublicationYear(publicationYear);
        this.setGenre(genre);
        this.setPrice(price);
    }

    // Getter and setter methods for the ISBN attribute with validation
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isValidIsbn(isbn)) {
            this.isbn = isbn;
        } else {
            throw new IllegalArgumentException("ISBN is not valid.");
        }
    }

    // Getter and setter methods for the Title attribute with validation
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (isValidTitle(title)) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Title is not valid.");
        }
    }

    // Getter and setter methods for the AuthorName attribute with validation
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        if (isValidAuthorName(authorName)) {
            this.authorName = authorName;
        } else {
            throw new IllegalArgumentException("Author name is not valid.");
        }
    }

    // Getter and setter methods for the PublicationYear attribute with validation
    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        if (isValidPublicationYear(publicationYear)) {
            this.publicationYear = publicationYear;
        } else {
            throw new IllegalArgumentException("Publication year is not valid.");
        }
    }

    // Getter and setter methods for the Genre attribute with validation
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (isValidGenre(genre)) {
            this.genre = genre;
        } else {
            throw new IllegalArgumentException("Genre is not valid.");
        }
    }

    // Getter and setter methods for the Price attribute with validation
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (isValidPrice(price)) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price is not valid.");
        }
    }

    // Private validation methods for each attribute
    private boolean isValidIsbn(String isbn) {
        // Implement ISBN validation logic here (e.g., length, format)
        return isbn != null && isbn.length() == 13;
    }

    private boolean isValidTitle(String title) {
        // Implement title validation logic here (e.g., length)
        return title != null && title.length() <= 100;
    }

    private boolean isValidAuthorName(String authorName) {
        // Implement author name validation logic here (e.g., length)
        return authorName != null && authorName.length() <= 100;
    }

    private boolean isValidPublicationYear(int publicationYear) {
        // Implement publication year validation logic here (e.g., reasonable year)
        return publicationYear >= 0 && publicationYear <= 9999;
    }

    private boolean isValidGenre(String genre) {
        // Implement genre validation logic here (e.g., length)
        return genre != null && genre.length() <= 30;
    }

    private boolean isValidCopiesAvailable(int copiesAvailable) {
        // Implement copies available validation logic here (e.g., non-negative)
        return copiesAvailable >= 0;
    }

    private boolean isValidPrice(BigDecimal price) {
        // Implement price validation logic here (e.g., non-negative)
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }


    @Override
    public String toString() {
        return "{ " +
            " isbn='" + getIsbn() + "'" +
            ", title='" + getTitle() + "'" +
            ", authorName='" + getAuthorName() + "'" +
            ", publicationYear='" + getPublicationYear() + "'" +
            ", genre='" + getGenre() + "'" +
            ", price='" + getPrice() + "'" +
            " }";
    }

}