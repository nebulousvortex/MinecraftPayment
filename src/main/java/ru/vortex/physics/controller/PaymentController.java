package ru.vortex.physics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vortex.physics.model.payment.Amount;
import ru.vortex.physics.model.payment.Confirmation;
import ru.vortex.physics.model.payment.Payment;
import ru.vortex.physics.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/payment/")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/getEmbeddedPayment")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public Payment createEmbeddedPayment() {
        Payment newPay = new Payment();
        newPay.setAmount(new Amount("1", "RUB"));
        newPay.setDescription("Тестовое описание для embedded");
        newPay.setCapture(true);
        newPay.setConfirmation(new Confirmation("embedded"));

        newPay = paymentService.createPayment(newPay);
        return newPay;
    }

    @PostMapping("/getRedirectPayment")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public Payment createPaymentRedirect() {
        Payment newPay = new Payment();
        newPay.setAmount(new Amount("1", "RUB"));
        newPay.setDescription("Тестовое описание для redirect");
        newPay.setCapture(true);
        newPay.setConfirmation(new Confirmation("redirect", "https://leafcity.ru/shop", "https://leafcity.ru/shop"));

        newPay = paymentService.createPayment(newPay);
        return newPay;
    }

    @PostMapping("/getConfirmationToken")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getConfirmationToken() {
        Payment newPay = new Payment();
        newPay.setAmount(new Amount("1", "RUB"));
        newPay.setCapture(true);
        newPay.setConfirmation(new Confirmation("embedded"));

        newPay = paymentService.createPayment(newPay);
        Map<String, String> response = new HashMap<>();
        response.put("confirmation_token", newPay.getConfirmation().getConfirmation_token());

        return ResponseEntity.ok(response);
    }
}
