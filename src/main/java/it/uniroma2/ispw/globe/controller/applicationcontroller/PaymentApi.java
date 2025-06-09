package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;

public class PaymentApi {

    // classe Mock che simula il comportamento dell'api
    public boolean processPayment(double amount, User payer, Agency payee){

        return amount < 1000 && payer.getPaymentCredential() != null && payee.getPaymentCredential() != null;
    }
}
