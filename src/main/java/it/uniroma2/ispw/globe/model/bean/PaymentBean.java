package it.uniroma2.ispw.globe.model.bean;

public class PaymentBean {
    private String payerID;
    private String payeeID;
    private double amount;

    public PaymentBean(String payerID, String payeeID, double amount) {
        this.payerID = payerID;
        this.payeeID = payeeID;
        this.amount = amount;
    }

    public String getPayerID() {
        return payerID;
    }
    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }
    public String getPayeeID() {
        return payeeID;
    }
    public void setPayeeID(String payeeID) {
        this.payeeID = payeeID;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
