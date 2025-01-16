package it.uniroma2.ispw.globe.controller.applicationcontroller;

import java.util.UUID;

public class PaymentApi {
    // classe Mock che simula il comportamento dell'api
    String processPayment(String payer, String payee,double amount){
        if (amount < 1000) {
            return UUID.randomUUID().toString();
        } else {
            return null;
        }
    }
}
