package com.eventpro.PaymentService.service.impl;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.PaymentService.producer.KafkaProducer;
import com.eventpro.PaymentService.service.PaymentService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger log = LogManager.getLogger(PaymentServiceImpl.class);
	
	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Override
	@CircuitBreaker(name = "paymentService", fallbackMethod = "fallbackCall")
	public void paymentProcess(final SaleDTO saleDto) {
		log.info("paymentProcess({})", saleDto);
		
		if (new Random().nextBoolean()) {
			log.info("PAYMENT SERVICE 1: SUCCESS! -> {}", saleDto);
			this.kafkaProducer.paymentSuccess(saleDto);
		} else {
			log.warn("PAYMENT SERVICE 1: FAILED! -> {}", saleDto);
			throw new RuntimeException();
			
		}
	}
	
	@SuppressWarnings("unused")
	private void fallbackCall(final SaleDTO saleDto, Throwable ex) {
		log.info("fallbackCall({},{})", saleDto, ex);
		
		if (new Random().nextBoolean()) {
			log.info("PAYMENT SERVICE 2: SUCCESS! -> {}", saleDto);
			this.kafkaProducer.paymentSuccess(saleDto);
		} else {
			log.warn("PAYMENT SERVICE 2: FAILED! -> {}", saleDto);
			this.kafkaProducer.paymentFail(saleDto);
		}
	}

}
