package com.example.moattravel.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.moattravel.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;

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

		Stripe.apiKey = stripeApiKey;

		Event event = null;

		try {
			event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
		} catch (SignatureVerificationException e) {
			return new ResponseEntity<>("Webhook error", HttpStatus.BAD_REQUEST);
		}

		if ("checkout.session.completed".equals(event.getType())) {
			stripeService.processSessionCompleted(event);
		}

		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	public void processSessionCompleted(Event event) {

		Session session = (Session) event.getDataObjectDeserializer()
				.deserializeUnsafe();

		String sessionId = session.getId();

		try {
			Session fullSession = Session.retrieve(
					sessionId,
					SessionRetrieveParams.builder()
							.addExpand("payment_intent")
							.build(),
					null);

			Map<String, String> metadata = fullSession.getMetadata();

			reservationService.create(metadata);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}