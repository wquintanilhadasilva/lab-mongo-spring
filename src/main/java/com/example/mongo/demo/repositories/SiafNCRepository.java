package com.example.mongo.demo.repositories;

import com.example.mongo.demo.docs.Customer;
import com.example.mongo.demo.docs.SiafNC;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SiafNCRepository extends MongoRepository<SiafNC, SiafNC.CompositeKey> {

    SiafNC findByNumero(Long numero);
    List<SiafNC> findByDataEmissao(LocalDate dataEmissao);

}
