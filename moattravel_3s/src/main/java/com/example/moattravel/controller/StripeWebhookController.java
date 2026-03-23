package com.example.moattravel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.moattravel.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {

    private final StripeService stripeService;

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        // Stripe APIキー設定
        Stripe.apiKey = stripeApiKey;

        try {
            // Webhookの署名検証とイベント取得
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // checkout.session.completed の場合に処理
            if ("checkout.session.completed".equals(event.getType())) {
                stripeService.processWebhook(payload, sigHeader);
            }

        } catch (SignatureVerificationException e) {
            
            return ResponseEntity.badRequest().body("Webhook error");
        }

        return ResponseEntity.ok("Success");
    }
}