package com.lukehere.app.cycle.pojo;

public class History {
    private int cycleNumber;
    private String borrowDate;
    private String returnDate;
    private String securityEmailAddress;

    private String registrationNumber;
    private String fullName;
    private String phoneNumber;

    private String borrowedFrom;
    private String returnedTo;

    public History() {
    }

    public History(int cycleNumber, String borrowDate, String returnDate, String securityEmailAddress, String registrationNumber, String fullName, String phoneNumber, String studentEmailAddress, String borrowedFrom, String returnedTo) {
        this.cycleNumber = cycleNumber;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.securityEmailAddress = securityEmailAddress;
        this.registrationNumber = registrationNumber;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.borrowedFrom = borrowedFrom;
        this.returnedTo = returnedTo;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getSecurityEmailAddress() {
        return securityEmailAddress;
    }

    public void setSecurityEmailAddress(String securityEmailAddress) {
        this.securityEmailAddress = securityEmailAddress;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBorrowedFrom() {
        return borrowedFrom;
    }

    public void setBorrowedFrom(String borrowedFrom) {
        this.borrowedFrom = borrowedFrom;
    }

    public String getReturnedTo() {
        return returnedTo;
    }

    public void setReturnedTo(String returnedTo) {
        this.returnedTo = returnedTo;
    }
}
