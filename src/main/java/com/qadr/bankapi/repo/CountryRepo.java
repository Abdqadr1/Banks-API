package com.qadr.bankapi.repo;

import com.qadr.bankapi.model.Bank;
import com.qadr.bankapi.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepo extends JpaRepository<Country, Integer> {

    List<Country> findByContinent(String continent);

    Optional<Country> findByName(String name);

    Optional<Country> findByCode(String code);

    List<Country> findByCallCode(String code);


    @Query("SELECT c FROM Country c WHERE " +
            "c.name LIKE %?1% OR " +
            "c.code LIKE %?1% OR " +
            "c.callCode LIKE %?1% OR " +
            "c.continent LIKE %?1%")
    Page<Country> searchCountries(String keyword, Pageable pageable);
}
