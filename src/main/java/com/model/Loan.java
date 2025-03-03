package com.model;
import java.time.LocalDate;


public class Loan {
    private int id;
    private int userId;       
    private int bookId;       
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean active;
    Long DAYS_TO_ADD=7L;


    public Loan() {
        this.loanDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(DAYS_TO_ADD);
        this.active = true;
    }

    /**
     * Constructor for creating a new loan
     * 
     * @param userId The ID of the borrowing user
     * @param bookId The ID of the book being borrowed
     */
    public Loan(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(DAYS_TO_ADD);
        this.active = true;
    }

    /**
     * Full constructor
     * 
     * @param id The loan ID
     * @param userId The user ID
     * @param bookId The book ID
     * @param loanDate The date the book was loaned
     * @param dueDate The date the book is due
     * @param returnDate The date the book was returned (can be null)
     * @param active Whether the loan is still active
     */
    public Loan(int id, int userId, int bookId, LocalDate loanDate, LocalDate dueDate, 
                LocalDate returnDate, boolean active) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.active = active;
    }

    /**
     * Returns the book, setting return date and active status
     */
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.active = false;
    }

    /**
     * Sets the loan as overdue by setting the due date to yesterday
     */

    public void setOverdue() {
        this.dueDate = LocalDate.now().minusDays(1);
    }

    /**
     * Calculates if the book is overdue
     * 
     * @return true if the book is overdue and still checked out
     */
    public boolean isOverdue() {
        return active && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculates the number of days overdue
     * 
     * @return The number of days the book is overdue, or 0 if not overdue
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + (returnDate != null ? returnDate : "Not returned") +
                ", active=" + active +
                '}';
    }
}