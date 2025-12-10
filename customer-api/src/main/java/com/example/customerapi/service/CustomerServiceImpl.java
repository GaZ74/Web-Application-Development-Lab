package com.example.customerapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.customerapi.dto.CustomerRequestDTO;
import com.example.customerapi.dto.CustomerResponseDTO;
import com.example.customerapi.dto.CustomerUpdateDTO;
import com.example.customerapi.entity.Customer;
import com.example.customerapi.entity.CustomerStatus;
import com.example.customerapi.exception.DuplicateResourceException;
import com.example.customerapi.exception.ResourceNotFoundException;
import com.example.customerapi.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToResponseDTO(customer);
    }
    
    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        // Check for duplicates
        if (customerRepository.existsByCustomerCode(requestDTO.getCustomerCode())) {
            throw new DuplicateResourceException("Customer code already exists: " + requestDTO.getCustomerCode());
        }
        
        if (customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
        }
        
        // Convert DTO to Entity
        Customer customer = convertToEntity(requestDTO);
        
        // Save to database
        Customer savedCustomer = customerRepository.save(customer);
        
        // Convert Entity to Response DTO
        return convertToResponseDTO(savedCustomer);
    }
    
    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        // Check if email is being changed to an existing one
        if (!existingCustomer.getEmail().equals(requestDTO.getEmail()) 
            && customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + requestDTO.getEmail());
        }
        
        // Update fields
        existingCustomer.setFullName(requestDTO.getFullName());
        existingCustomer.setEmail(requestDTO.getEmail());
        existingCustomer.setPhone(requestDTO.getPhone());
        existingCustomer.setAddress(requestDTO.getAddress());
        
        // Don't update customerCode (immutable)
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponseDTO(updatedCustomer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
    
    @Override
    public List<CustomerResponseDTO> searchCustomers(String keyword) {
        return customerRepository.searchCustomers(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CustomerResponseDTO> getCustomersByStatus(String status) {
        try {
            CustomerStatus customerStatus = CustomerStatus.valueOf(status.toUpperCase());
            return customerRepository.findByStatus(customerStatus)
                    .stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status: " + status);
        }
    }
    
    @Override
    public List<CustomerResponseDTO> advancedSearch(String name, String email, String status) {
        List<Customer> results = customerRepository.findAll();
        
        // Filter by name if provided
        if (name != null && !name.trim().isEmpty()) {
            results = results.stream()
                    .filter(c -> c.getFullName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Filter by email if provided
        if (email != null && !email.trim().isEmpty()) {
            results = results.stream()
                    .filter(c -> c.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Filter by status if provided
        if (status != null && !status.trim().isEmpty()) {
            try {
                CustomerStatus customerStatus = CustomerStatus.valueOf(status.toUpperCase());
                results = results.stream()
                        .filter(c -> c.getStatus() == customerStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new ResourceNotFoundException("Invalid status: " + status);
            }
        }
        
        return results.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<CustomerResponseDTO> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(this::convertToResponseDTO);
    }
    
    @Override
    public Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        // Default to sorting by id if sortBy is not provided
        String sortField = sortBy != null && !sortBy.trim().isEmpty() ? sortBy : "id";
        
        // Validate sort field to prevent invalid column names
        String[] validFields = {"id", "fullName", "email", "customerCode", "phone", "status", "createdAt", "updatedAt"};
        boolean isValidField = false;
        for (String field : validFields) {
            if (field.equals(sortField)) {
                isValidField = true;
                break;
            }
        }
        
        if (!isValidField) {
            sortField = "id"; // Default to id if invalid field is provided
        }
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(this::convertToResponseDTO);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers(String sortBy, String sortDir) {
        // Default to sorting by id if sortBy is not provided
        String sortField = sortBy != null && !sortBy.trim().isEmpty() ? sortBy : "id";

        // Validate sort field to prevent invalid column names
        String[] validFields = {"id", "fullName", "email", "customerCode", "phone", "status", "createdAt", "updatedAt"};
        boolean isValidField = false;
        for (String field : validFields) {
            if (field.equals(sortField)) {
                isValidField = true;
                break;
            }
        }

        if (!isValidField) {
            sortField = "id"; // Default to id if invalid field is provided
        }

        Sort.Direction direction = sortDir != null && sortDir.equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);
        List<Customer> customers = customerRepository.findAll(sort);
        return customers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    // Helper Methods for DTO Conversion
    
    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(customer.getId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setStatus(customer.getStatus().toString());
        dto.setCreatedAt(customer.getCreatedAt());
        return dto;
    }
    
    private Customer convertToEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerCode(dto.getCustomerCode());
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        return customer;
    }

    @Override
    public CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // If email provided and changed, ensure uniqueness
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()) {
            String newEmail = updateDTO.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(customer.getEmail()) && customerRepository.existsByEmail(newEmail)) {
                throw new DuplicateResourceException("Email already exists: " + newEmail);
            }
            customer.setEmail(newEmail);
        }

        if (updateDTO.getFullName() != null) {
            customer.setFullName(updateDTO.getFullName());
        }
        if (updateDTO.getPhone() != null) {
            customer.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getAddress() != null) {
            customer.setAddress(updateDTO.getAddress());
        }

        Customer saved = customerRepository.save(customer);
        return convertToResponseDTO(saved);
    }
}
