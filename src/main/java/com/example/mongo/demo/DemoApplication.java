package com.example.mongo.demo;

import com.example.mongo.demo.docs.Customer;
import com.example.mongo.demo.docs.SiafNC;
import com.example.mongo.demo.repositories.CustomerRepository;
import com.example.mongo.demo.repositories.SiafNCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.time.LocalDate;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;
	@Autowired
	private SiafNCRepository siafNCRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		repository.deleteAll();

		// save a couple of customers
		repository.save(new Customer("Alice", "Smith"));
		repository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Customer customer : repository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : repository.findByLastName("Smith")) {
			System.out.println(customer);
		}

//		siafNCRepository.deleteAll();
		this.createNCs();
		for (SiafNC siafNC : siafNCRepository.findAll()) {
			System.out.println(siafNC);
		}
		System.out.println();

	}

	private void createNCs() {
		for (int i = 0; i < 10; i++){
			SiafNC s = SiafNC.builder()
					.dataEmissao(LocalDate.now())
					.esfera("Esfera" + i)
					.evento("Evento " + i)
					.fonte("Fonte"+i)
					.numero(Long.parseLong(String.valueOf(i)))
					.pi("PI " + i)
					.observacao("Observação " + i)
					.ptres("PTRES "+ i)
					.ugEmitente("UGEMitente" + i)
					.ugGestaoFavorecida("UGGestaoFavorecida "+ i)
					.ugr("UGR " + i)
					.valor(Double.parseDouble("20.00"))
					.build();
			s.buildId();
			siafNCRepository.save(s);

		}
	}
}
