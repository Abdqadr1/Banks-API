package com.qadr.bankapi.repo;

import com.qadr.bankapi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {

    List<Country> findByContinent(String continent);

    Optional<Country> findByName(String name);

    Optional<Country> findByCode(String code);

    Optional<Country> findByCallCode(String code);
}
