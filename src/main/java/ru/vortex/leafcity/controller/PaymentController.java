package ru.vortex.leafcity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vortex.leafcity.model.payment.Amount;
import ru.vortex.leafcity.model.payment.Confirmation;
import ru.vortex.leafcity.model.payment.Payment;
import ru.vortex.leafcity.model.payment.PaymentMeta;
import ru.vortex.leafcity.model.request.UserProductRequest;
import ru.vortex.leafcity.model.shop.Product;
import ru.vortex.leafcity.service.PaymentService;
import ru.vortex.leafcity.service.ShopService;

import java.text.ParseException;
import java.util.*;

@RestController()
@CrossOrigin(origins = {"http://localhost:3000" , "https://leafcity.vercel.app", "https://leafcity.ru"})
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShopService shopService;


    @PostMapping("/getRedirectPayment")
    @ResponseBody
    public ResponseEntity<?> createPaymentRedirect(@RequestBody UserProductRequest userProductRequest) {
        Payment newPay = new Payment();
        Product product = shopService.getProductById(userProductRequest.getProductId());
        if(product != null) {
            newPay.setAmount(new Amount(Float.toString(product.getRealPrice()), "RUB"));
            newPay.setDescription(product.getName());
            newPay.setCapture(true);
            newPay.setMetadata(new PaymentMeta(userProductRequest.getUsername(), product.getId(), product.getName()));
            newPay.setConfirmation(new Confirmation("redirect", "", userProductRequest.getRedirectUrl()));
            newPay = paymentService.createPayment(newPay);

            Map<String, String> response = new HashMap<>();
            response.put("confirmation_url", newPay.getConfirmation().getConfirmation_url());
            return ResponseEntity.ok(response);
        }
        return  ResponseEntity.badRequest().body("продукт не найден!");
    }
    @GetMapping("/getPayments")
    @ResponseBody
    public List<Payment> getPayments() {
        return paymentService.getPayments();
    }

    @GetMapping("/getPayment")
    @ResponseBody
    public Payment getPayment(@RequestParam  String id) {
        return paymentService.getPayment(id);
    }

    @GetMapping("/getLastPayments")
    @ResponseBody
    public List<Map<String, Object>> getLastPayments() throws ParseException {
        return paymentService.getLastPayments();
    }
}
