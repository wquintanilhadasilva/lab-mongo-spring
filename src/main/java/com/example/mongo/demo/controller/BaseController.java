package com.example.mongo.demo.controller;

import com.example.mongo.demo.docs.Customer;
import com.example.mongo.demo.docs.SiafNC;
import com.example.mongo.demo.model.APIResponse;
import com.example.mongo.demo.repositories.CustomerRepository;
import com.example.mongo.demo.repositories.SiafNCRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@AllArgsConstructor
public class BaseController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SiafNCRepository siafNCRepository;
	
	@GetMapping(value = "/base")
    public ResponseEntity<APIResponse> readBaseResponse () {
        return ResponseEntity.ok(new APIResponse("API Running"));
    }

    @GetMapping(value = "/appcheck")
    public ResponseEntity<APIResponse> checkApp () {
        return ResponseEntity.ok(new APIResponse("API Running Checked"));
    }

    @GetMapping(value = "/customers")
    public ResponseEntity<List<Customer>> customers () {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping(value = "/siafs")
    public ResponseEntity<List<SiafNC>> siafs () {
        return ResponseEntity.ok(siafNCRepository.findAll());
    }

}
