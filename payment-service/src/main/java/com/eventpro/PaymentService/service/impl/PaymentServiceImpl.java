package com.eventpro.PaymentService.service.impl;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.PaymentService.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger log = LogManager.getLogger(PaymentServiceImpl.class);
	
	@Override
	public void paymentProcess(final SaleDTO saleDto) {
		log.debug("paymentProcess({})", saleDto);
		
		if (new Random().nextBoolean()) {
			log.info("PAYMENT COMPLETED WITH SUCCESS! -> {}", saleDto);
		} else {
			log.warn("PAYMENT FAILED! -> {}", saleDto);
		}
	}

}
