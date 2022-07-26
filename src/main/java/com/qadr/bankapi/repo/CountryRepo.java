package com.qadr.bankapi.repo;

import com.qadr.bankapi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {

    List<Country> findByContinent(String continent);
}
