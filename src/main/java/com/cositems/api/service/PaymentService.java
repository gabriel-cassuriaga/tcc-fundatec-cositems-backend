package com.cositems.api.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cositems.api.exception.PaymentFailedException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {
    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String processCreditCardPayment(BigDecimal amount, String paymentToken) {

        Long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("brl")
                .setSource(paymentToken)
                .setDescription("Cobrança de pedido da CosItems API")
                .build();

        try {
            Charge charge = Charge.create(params);

            if (charge.getPaid() != null && charge.getPaid().booleanValue()) {
                return charge.getId();
            } else {
                throw new PaymentFailedException("Pagamento recusado: " + charge.getFailureMessage());
            }

        } catch (StripeException e) {
            throw new PaymentFailedException("Erro na comunicação com o Gateway de Pagamento: " + e.getMessage());
        }
    }
}