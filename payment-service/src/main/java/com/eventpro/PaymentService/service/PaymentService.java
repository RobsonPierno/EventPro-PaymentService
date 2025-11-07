package com.eventpro.PaymentService.service;

import com.eventpr.SalesService.dto.SaleDTO;

public interface PaymentService {

	public void paymentProcess(final SaleDTO saleDto);
}
