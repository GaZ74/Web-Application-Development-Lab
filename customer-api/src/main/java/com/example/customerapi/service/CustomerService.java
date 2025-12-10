package com.example.customerapi.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.customerapi.dto.CustomerRequestDTO;
import com.example.customerapi.dto.CustomerResponseDTO;
import com.example.customerapi.dto.CustomerUpdateDTO;

public interface CustomerService {
    
    List<CustomerResponseDTO> getAllCustomers();
    
    CustomerResponseDTO getCustomerById(Long id);
    
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);
    
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);
    
    void deleteCustomer(Long id);
    
    List<CustomerResponseDTO> searchCustomers(String keyword);
    
    List<CustomerResponseDTO> getCustomersByStatus(String status);
    
    List<CustomerResponseDTO> advancedSearch(String name, String email, String status);

    Page<CustomerResponseDTO> getAllCustomers(int page, int size);
    
    Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir);
    
    List<CustomerResponseDTO> getAllCustomers(String sortBy, String sortDir);

    CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO);
}
