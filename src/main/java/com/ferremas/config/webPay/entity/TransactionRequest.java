package com.ferremas.config.webPay.entity;

public class TransactionRequest {
    private String buyOrder;
    private String sessionId;
    private double amount;
    private String returnUrl;

    public TransactionRequest(String buyOrder, String sessionId, double amount, String returnUrl) {
        this.buyOrder = buyOrder;
        this.sessionId = sessionId;
        this.amount = amount;
        this.returnUrl = returnUrl;
    }

    public TransactionRequest() {
    }

    // Getters y setters
    public String getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(String buyOrder) {
        this.buyOrder = buyOrder;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
