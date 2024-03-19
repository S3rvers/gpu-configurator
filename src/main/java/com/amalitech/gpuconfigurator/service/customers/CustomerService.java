package com.amalitech.gpuconfigurator.service.customers;


import com.amalitech.gpuconfigurator.dto.customers.CustomerResponseDto;
import com.amalitech.gpuconfigurator.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Page<CustomerResponseDto> getAllCustomers(Integer page, Integer size);
}
