package com.eventpro.PaymentService.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.eventpr.SalesService.dto.SaleDTO;

@Component
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, SaleDTO> kafkaTemplate;
	
	@Value("${kafka.topic.payment.success}")
	private String paymentSuccessTopic;
	
	@Value("${kafka.topic.payment.fail}")
	private String paymentFailTopic;
	
	public void paymentSuccess(final SaleDTO saleDto) {
		String key = String.format("AttendeeId:%s|TicketId:%s}", saleDto.attendeeId(), saleDto.ticketId());
		this.kafkaTemplate.send(this.paymentSuccessTopic, key, saleDto);
	}
	
	public void paymentFail(final SaleDTO saleDto) {
		String key = String.format("AttendeeId:%s|TicketId:%s}", saleDto.attendeeId(), saleDto.ticketId());
		this.kafkaTemplate.send(this.paymentFailTopic, key, saleDto);
	}
}
