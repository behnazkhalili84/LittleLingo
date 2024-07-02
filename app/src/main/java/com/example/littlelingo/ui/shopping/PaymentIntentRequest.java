package com.example.littlelingo.ui.shopping;

public class PaymentIntentRequest {
    private int amount;
    private String currency;

    public  PaymentIntentRequest(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }       


}
