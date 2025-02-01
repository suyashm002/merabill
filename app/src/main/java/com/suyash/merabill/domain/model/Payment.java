package com.suyash.merabill.domain.model;

public class Payment {
    private String type;
    private double amount;
    private String provider;
    private String transactionReference;

    public Payment(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    // Constructor for bank transfer and credit card payments
    public Payment(String type, double amount, String provider, String transactionReference) {
        this(type, amount);
        this.provider = provider;
        this.transactionReference = transactionReference;
    }

    // Getters and setters
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getProvider() { return provider; }
    public String getTransactionReference() { return transactionReference; }

    public String getDisplayText() {
        return type + " = Rs. " + String.format("%.2f", amount);
    }
}