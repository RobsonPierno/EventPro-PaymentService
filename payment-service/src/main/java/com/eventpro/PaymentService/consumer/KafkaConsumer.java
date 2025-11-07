package com.eventpro.PaymentService.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.eventpr.SalesService.dto.SaleDTO;
import com.eventpro.PaymentService.service.PaymentService;

@Component
public class KafkaConsumer {
	
	private static final Logger log = LogManager.getLogger(KafkaConsumer.class);
	
	@Autowired
	private PaymentService paymentService;

    @KafkaListener(
    		topics = "${kafka.topic.ticket.sale.created}",
    	    containerFactory = "ticketSaleCreatedKafkaListener"
    )
    public void ticketSaleCreatedListener(final SaleDTO saleDto) {
        log.info("ticketSaleCreatedListener({})", saleDto);
        this.paymentService.paymentProcess(saleDto);
    }
}
