package it.uniroma2.ispw.globe.model.bean;

public class PaymentBean {
    private String payerUsername;
    private String payeeUsername;
    private double amount;

    public PaymentBean(String payerUsername, String payeeUsername, double amount) {
        this.payerUsername = payerUsername;
        this.payeeUsername = payeeUsername;
        this.amount = amount;
    }

    public String getPayerUsername() {
        return payerUsername;
    }
    public void setPayerUsername(String payerUsername) {
        this.payerUsername = payerUsername;
    }
    public String getPayeeUsername() {
        return payeeUsername;
    }
    public void setPayeeUsername(String payeeUsername) {
        this.payeeUsername = payeeUsername;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
